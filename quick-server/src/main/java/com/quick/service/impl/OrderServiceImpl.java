package com.quick.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.quick.WebSockert.WebSocketServer;
import com.quick.constant.MessageConstant;
import com.quick.context.BaseContext;
import com.quick.dto.*;
import com.quick.entity.*;
import com.quick.exception.AddressBookBusinessException;
import com.quick.exception.OrderBusinessException;
import com.quick.exception.ShoppingCartBusinessException;
import com.quick.mapper.*;
import com.quick.result.PageResult;
import com.quick.service.OrderService;
import com.quick.utils.WeChatPayUtil;
import com.quick.vo.OrderPaymentVO;
import com.quick.vo.OrderStatisticsVO;
import com.quick.vo.OrderSubmitVO;
import com.quick.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;


    private static final String shopAddress ="Hutb";
    private static final String ak = "TGEXK8hpG526ROvyZd93ck0jQIasep8g";




    /*
    * 用户下单
     */
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //0.处理业务异常（地址为空，购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        //检查用户的地址是否超出配送范围
//        checkOutOfRange(addressBook.getCityName()+addressBook.getDistrictName()+ addressBook.getDetail());

        //查询用户购物车数据

        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list == null || list.size() == 0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //1.向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);

        List<OrderDetail> orderDetails = new ArrayList<>();
        //2.向订单明细表插入n条数据
        for(ShoppingCart cart : list){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
            //获取的前提是，orderMapper。xml中配置了keyProperty="id"
            //设置订单id
        }

        orderDetailMapper.insertBatch(orderDetails);
        //3.用户下单成功后，清空购物车数据

        shoppingCartMapper.deleteByUserId(userId);
        //4.封装vo返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();


        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 直接返回支付成功响应，不生成任何微信支付相关参数
        // 这样前端就能直接处理支付成功逻辑，而不是尝试调用微信支付接口
        
        // 直接调用支付成功方法更新订单状态
        this.paySuccess(ordersPaymentDTO.getOrderNumber());
        
        OrderPaymentVO vo = new OrderPaymentVO();
        // 设置必要的字段，避免空指针异常
        vo.setNonceStr(generateRandomString(16));
        vo.setPaySign("MOCK_SIGN_");
        vo.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        vo.setSignType("MOCK");
        vo.setPackageStr("MOCK_PACKAGE");
        // 直接设置skipRealPayment为true，不再使用反射
        vo.setSkipRealPayment(true);
        
        // 打印详细日志，方便调试
        System.out.println("模拟支付成功，订单号：" + ordersPaymentDTO.getOrderNumber() + ", 返回数据：" + vo.toString());
        
        return vo;
    }
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //通过websocket发送消息给客户端 type orderid content
        Map map = new HashMap();
        map.put("type", 1);//1表示来单提醒，2表示客户催单
        map.put("orderId", ordersDB.getId());//订单id
        map.put("content", "来单提醒:"+outTradeNo);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
        
        // 打印模拟支付成功日志
        System.out.println("订单支付成功，订单号：" + outTradeNo + "，状态更新为待接单");
    }

    @Override
    public PageResult historyOrders(Integer page, Integer pageSize, Integer status) {
        //1.设置分页的参数，
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setStatus(status);
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        //2.获取分页数据
        List<OrderVO> list = new ArrayList<>();
        Page<Orders> pageInfo = orderMapper.pageQuery(ordersPageQueryDTO);
        //3.返回总条数，和查询到的数据，数据为历史订单封装成的列表
        if(pageInfo!= null && pageInfo.getTotal()> 0){
            for(Orders orders : pageInfo){
                //获取订单id
                Long orderId = orders.getId();
                //获取订单详情
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);//把订单数据复制到orderVO
                orderVO.setOrderDetailList(orderDetailList);

                list.add(orderVO);
            }
        }
        return new PageResult(pageInfo.getTotal(), list);
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        //根据id查询订单数据
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //查询订单对应的菜品/套餐明细
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());
        // 将订单和详情封装到vo中并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void useCancel(Long orderId) throws Exception {
        //1.查询当前订单的状态
        Orders ordersDB = orderMapper.getById(orderId);

        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //- 2.商家已接单状态下，用户取消订单需电话沟通商家
        //- 派送中状态下，用户取消订单需电话沟通商家
        //- 如果在待接单状态下取消订单，需要给用户退款
        //- 取消订单后需要将订单状态修改为“已取消”

        if(ordersDB.getStatus().equals(Orders.CONFIRMED) || ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            //3.商家已接单状态下，用户取消订单需电话沟通商家
            //4.派送中状态下，用户取消订单需电话沟通商家
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders =new Orders();
        orders.setId(orderId);
        if(ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            // 模拟退款，跳过实际的退款流程
            // 由于缺少商户ID等配置，不调用真实的退款接口
            System.out.println("模拟退款成功，订单号：" + ordersDB.getNumber());
            orders.setPayStatus(Orders.REFUND);
        }
        //6.取消订单后需要将订单状态修改为“已取消”,订单状态，取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);

    }

    @Override
    public void repetition(Long id) {
        //再来一单就是把订单数据重新插入到购物车当中
        Long userId = BaseContext.getCurrentId();//查询当前用户的id

        //根据订单id，查询订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        //把订单详情数据，重新插入到购物车中
        List<ShoppingCart> shoppingCartList = orderDetails.stream().map(
                order ->{
                    ShoppingCart shoppingCart = new ShoppingCart();
                    //把原订单的菜品或者套餐数据，复制到购物车中
                    BeanUtils.copyProperties(order,shoppingCart,"id");
                    shoppingCart.setUserId(userId);
                    shoppingCart.setCreateTime(LocalDateTime.now());
                    return shoppingCart;
                }).collect(Collectors.toList());

        shoppingCartMapper.insertBatch(shoppingCartList);
        }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        //- 输入订单号/手机号进行搜索，支持模糊搜索
        //- 根据订单状态进行筛选
        //- 下单时间进行时间筛选
        //- 搜索内容为空，提示未找到相关订单
        //- 搜索结果页，展示包含搜索关键词的内容
        //- 分页展示搜索到的订单数据
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> list = getOrderVOList(page);

        return new PageResult(page.getTotal(), list);
    }

    @Override
    public OrderStatisticsVO statistic() {
        //分别统计待派送，派送中，待接单数量用confirmed，deliveryProgress,toBeConfirmed表示
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        //封装数据
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        return orderStatisticsVO;

    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        //接单的逻辑就是把订单状态修改已接单
        Orders orders =Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
                orderMapper.update(orders);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        //拒单逻辑是：将订单状态修改为“已取消”
        //- 只有订单处于“待接单”状态时可以执行拒单操作
        //- 商家拒单时需要指定拒单原因
        //- 商家拒单时，如果用户已经完成了支付，需要为用户退款
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Integer payStatus = ordersDB.getPayStatus();
        if(payStatus == Orders.PAID){
            // 模拟退款，跳过实际的退款流程
            // 由于缺少商户ID等配置，不调用真实的退款接口
            System.out.println("模拟拒单退款成功，订单号：" + ordersDB.getNumber());
        }
        //拒单要退款，根据id更新订单的状态，原因和取消时间
        Orders orders = Orders.builder().
                id(ordersDB.getId())
                .status(Orders.CANCELLED)
                .cancelReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now()).build();
        orderMapper.update(orders);

    }

    @Override
    public void adminCancel(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        //取消订单其实就是将订单状态修改为“已取消”
        //- 商家取消订单时需要指定取消原因
        //- 商家取消订单时，如果用户已经完成了支付，需要为用户退款
        //1.根据获取当前订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());
        //获取订单状态
        Integer payStatus = ordersDB.getPayStatus();
        if(payStatus == Orders.PAID){
            // 模拟退款，跳过实际的退款流程
            // 由于缺少商户ID等配置，不调用真实的退款接口
            System.out.println("模拟商家取消订单退款成功，订单号：" + ordersDB.getNumber());
        }
        //取消订单要退款，根据id更新订单的状态，原因和取消时间
        Orders orders = Orders.builder().
                id(ordersDB.getId())
                .status(Orders.CANCELLED)
                .cancelReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now()).build();
                orderMapper.update(orders);

    }

    @Override
    public void delivery(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        //校验订单是否存在，订单状态是否为“待接单”
        if(ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .deliveryTime(LocalDateTime.now())
                .build();
                orderMapper.update(orders);
    }

    @Override
    public void complete(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        //校验订单是否存在，订单状态是否为“4”
        if(ordersDB == null ||
                !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException
                    (MessageConstant.ORDER_NOT_FOUND);
        }
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();
                orderMapper.update(orders);
    }

    @Override
    public void reminder(Long id) {
        //根据id查询订单
        Orders ordersDB = orderMapper.getById(id);
        //校验订单是否存在，订单状态是否
        if(ordersDB == null){
            throw new OrderBusinessException
                    (MessageConstant.ORDER_NOT_FOUND);
        }
        Map map = new HashMap();
        map.put("type",2);//1表示来单提醒，2表示订单催单
        map.put("orderId",id);
        map.put("content","订单号："+ordersDB.getNumber());

        String json = JSON.toJSONString(map);


        webSocketServer.sendToAllClient(json);
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        //返回订单的菜品信息，自定义vo封装
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if(!CollectionUtils.isEmpty(ordersList)){
            for(Orders orders : ordersList){
                //把共同字段复制到vo
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                String orderDishes = getOrderDishesStr( orders);

                //将订单信息封装到 vo，并添加到orderVoList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }
    /*
    根据订单id获取订单菜品信息字符串
     */

    // 使用StringBuilder提高性能
    private String getOrderDishesStr(Orders orders) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        StringBuilder sb = new StringBuilder();
        for (OrderDetail detail : orderDetailList) {
            sb.append(detail.getName())
                    .append("*")
                    .append(detail.getNumber())
                    .append(";");
        }
        return sb.toString();
    }
    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address
     */
//    private void checkOutOfRange(String address) {
//        Map map = new HashMap();
//        map.put("address",shopAddress);
//        map.put("output","json");
//        map.put("ak",ak);
//
//        //获取店铺的经纬度坐标
//        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
//
//        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("店铺地址解析失败");
//        }
//
//        //数据解析
//        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
//        String lat = location.getString("lat");
//        String lng = location.getString("lng");
//        //店铺经纬度坐标
//        String shopLngLat = lat + "," + lng;
//
//        map.put("address",address);
//        //获取用户收货地址的经纬度坐标
//        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
//
//        jsonObject = JSON.parseObject(userCoordinate);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("收货地址解析失败");
//        }
//
//        //数据解析
//        location = jsonObject.getJSONObject("result").getJSONObject("location");
//        lat = location.getString("lat");
//        lng = location.getString("lng");
//        //用户收货地址经纬度坐标
//        String userLngLat = lat + "," + lng;
//
//        map.put("origin",shopLngLat);
//        map.put("destination",userLngLat);
//        map.put("steps_info","0");
//
//        //路线规划
//        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);
//
//        jsonObject = JSON.parseObject(json);
//        if(!jsonObject.getString("status").equals("0")){
//            throw new OrderBusinessException("配送路线规划失败");
//        }
//
//        //数据解析
//        JSONObject result = jsonObject.getJSONObject("result");
//        JSONArray jsonArray = (JSONArray) result.get("routes");
//        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
//
//        if(distance > 5000){
//            //配送距离超过5000米
//            throw new OrderBusinessException("超出配送范围");
//        }
//    }


}
