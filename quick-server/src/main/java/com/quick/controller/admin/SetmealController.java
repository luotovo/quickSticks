package com.quick.controller.admin;


import com.quick.dto.SetmealDTO;
import com.quick.dto.SetmealPageQueryDTO;
import com.quick.result.PageResult;
import com.quick.result.Result;
import com.quick.service.SetmealService;
import com.quick.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    /*
    分页查询套餐
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> queryPage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询,{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.queryPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    /*
    新增套餐

     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(value = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐，参数{}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }
    /*
    批量删除套餐
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(value = "setmealCache",allEntries = true)//所有缓存数据都清除
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐，ids：{}", ids);
        setmealService.delete(ids);
        return Result.success();
    }
    /*
    根据id查询套餐
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")

    public Result<SetmealVO> getById(@PathVariable  Long id){
        log.info("查询套餐，id：{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }
    /*
    修改套餐

     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(value = "setmealCache",allEntries = true)//所有缓存数据都清除
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐，参数{}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /*
    启用禁用

     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用")
    @CacheEvict(value = "setmealCache",allEntries = true)//所有缓存数据都清除
    public Result StatusSet ( @PathVariable Integer status, Long id){
        log.info("启用禁用套餐，id：{}", id);
        setmealService.startOrStop(status, id);
        return Result.success();

    }









}
