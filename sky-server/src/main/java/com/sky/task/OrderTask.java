package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/*
* 定时任务类,定时处理订单状态
 */
@Slf4j
@Component
public class OrderTask {
    /*
    * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟执行一次
    public void processTimeoutOrder(){
        log.info("处理支付超时订单");
    }
}
