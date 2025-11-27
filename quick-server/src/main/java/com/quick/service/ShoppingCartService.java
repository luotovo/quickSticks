package com.quick.service;

import com.quick.dto.ShoppingCartDTO;
import com.quick.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /*
    添加购物车

     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /*
    查看购物车
     */
    List<ShoppingCart> showShoppingCart();

    /*
    清空购物车
     */
    void cleanShoppingCart();

    /*
    删除购物车中的商品
     */
    void delete(ShoppingCartDTO shoppingCartDTO);
}
