package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/*
* 定时任务类,定时处理订单状态
 */
@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /*
    * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟执行一次
    public void processTimeoutOrder(){
        log.info("处理支付超时订单：{}", LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now().plusMinutes(-15);

        // select * from orders where status =1 and order_time <  now -15
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, now);

        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("支付超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
    /*
    * 一直处理处于派送中的订单
     */

    @Scheduled(cron="0 0 1 * * ?") //每晚1点执行一次
//    @Scheduled(cron = "1/5 * * * * ?")
    public void processDeliveryOrder(){
        log.info("处理处于派送中的订单：{}", LocalDateTime.now());
        // select * from orders where status = 3
        LocalDateTime now = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, now);
        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.COMPLETED);

                orderMapper.update(orders);
            }
        }

    }
}
