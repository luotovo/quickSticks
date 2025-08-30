package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService {

    /*
    * 套餐分页查询
     */
    PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /*
    * 新增套餐,同时需要保存套餐和菜品的关联关系
     */
    void saveWithDish(SetmealDTO setmealDTO);
    /*
    * 批量删除套餐
     */

    void delete(List<Long> ids);

    /*
    * 根据id查询套餐和套餐中的菜品信息
     */
    SetmealVO getByIdWithDish(Long id);

    /*
    * 修改套餐,同时需要保存套餐和菜品的关联关系

     */
    void updateWithDish(SetmealDTO setmealDTO);

    /*
    * 批量起售停售
    * @param status
     */
    void startOrStop(Integer status, Long id);
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
