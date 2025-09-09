package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户模块订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping ("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);

    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }
    /*
    分页查询历史订单
     */
    @GetMapping("/historyOrders")
    @ApiOperation("分页查询历史订单")
    public Result<PageResult> historyOrders( int page, int pageSize, Integer status){
        log.info("分页查询历史订单,{}", page, pageSize,status);
        PageResult pageResult = orderService.historyOrders(page, pageSize,status);

        return Result.success(pageResult);
    }
    /*
    查询订单的详情
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单的详情")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long OrderId){
        log.info("查询订单详情，订单id为{}", OrderId);
        OrderVO ordersVO = orderService.getOrderDetail(OrderId);
        return Result.success(ordersVO);
    }
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result userCancel(@PathVariable("id") Long OrderId) throws Exception {
        log.info("取消订单，订单id为{}", OrderId);
        orderService.useCancel(OrderId);
        return Result.success();

    }
    /*
    再来一单
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable("id") Long id){
        log.info("再来一单，订单id为{}", id);
        orderService.repetition(id);
        return Result.success();
    }
    /*
    催单
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("用户催单")
    public Result reminder(@PathVariable("id") Long id){
        log.info("催单，订单id为{}", id);
        orderService.reminder(id);
        return Result.success();
    }


}
