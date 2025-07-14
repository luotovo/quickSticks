package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
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

    SetmealVO getByIdWithDish(Long id);

    void updateWithDish(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);
}
