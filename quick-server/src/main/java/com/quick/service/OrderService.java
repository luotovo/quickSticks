package com.quick.service;

import com.quick.dto.*;
import com.quick.result.PageResult;
import com.quick.vo.OrderPaymentVO;
import com.quick.vo.OrderStatisticsVO;
import com.quick.vo.OrderSubmitVO;
import com.quick.vo.OrderVO;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

public interface   OrderService {
    /*
    提交订单
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /*
    历史订单查询
     */
    PageResult historyOrders(Integer page, Integer pageSize, Integer status);

    /*
    订单详情查询
     */
    OrderVO getOrderDetail(Long orderId);

    /*
    取消订单
     */
    void useCancel(Long orderId) throws Exception;

    /*
    再来一单
     */
    void repetition(Long id);

    /*
    订单搜索
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
    各个状态订单数量统计接口
     */
    OrderStatisticsVO statistic();

    /*
    接单
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /*
    拒单
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /*
    商家取消订单
     */
    void adminCancel(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /*
    配送订单
     */
    void delivery(Long id);

    /*
    完成订单
     */
    void complete(Long id);

    /*
    用户催单
     */
    void reminder(Long id);
}
