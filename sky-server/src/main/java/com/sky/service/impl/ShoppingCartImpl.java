package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishServiceMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /*
    * 添加购物车
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车的商品是否已经存在
        ShoppingCart shoppingcart =new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingcart);//前面拷贝到后面
        Long userId = BaseContext.getCurrentId();
        shoppingcart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingcart);

        //如果存在了，则数量加1
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //不存在则插入一条 数据
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId != null){
                //本次添加的是菜品、
                Dish dish = dishServiceMapper.getById(dishId);
                shoppingcart.setName(dish.getName());
                shoppingcart.setImage(dish.getImage());
                shoppingcart.setAmount(dish.getPrice());


            }else{
                //本次添加的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingcart.setName(setmeal.getName());
                shoppingcart.setImage(setmeal.getImage());
                shoppingcart.setAmount(setmeal.getPrice());

            }
            shoppingcart.setNumber(1);
            shoppingcart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingcart);


        }
    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 获取当前用户的id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart =  ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingCartMapper.list(cart);
        return list;

    }

    @Override
    public void cleanShoppingCart() {
        //先获取当前用户的id
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);//前面拷贝到后面
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果在购物车中的数量是1，则直接删除商品
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);

            Integer number = cart.getNumber();
            if (number == 1) {
                shoppingCartMapper.deleteById(cart.getId());
            }
            else {
                //当前商品在购物车中的份数不为1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }




}
