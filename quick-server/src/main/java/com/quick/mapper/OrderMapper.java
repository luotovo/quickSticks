package com.quick.mapper;

import com.github.pagehelper.Page;
import com.quick.dto.GoodsSalesDTO;
import com.quick.dto.OrdersPageQueryDTO;
import com.quick.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /*
    * 插入订单数据
     */

    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);


    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /*
    * 根据id查询订单
     */
    Orders getById(Long orderId);
    /*
    * 根据状态统计订单数量
     */

    Integer countStatus(Integer status);

    /*
    根据状态和下单时间查询订单·
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /*
    统计订单表中一段时间内的营业额数量
     */
    Double sumByMap(Map map);
    /*
    统计订单表中一段时间内订单数量
     */
    Integer countByMap(Map map);
    /*
    统计指定时间内的销量前10
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
