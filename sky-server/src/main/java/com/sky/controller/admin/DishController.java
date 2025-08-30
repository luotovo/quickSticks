package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        clearCache(key);

        return Result.success();
    }
    /**
     * 菜品分页查询
     * @param
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询,{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        // requestparam用来接收参数
        log.info("批量删除菜品，ids：{}", ids);
        dishService.delete(ids);

        //将所有的菜品缓存数据都删除，所有dish_开头的key
        clearCache("dish_*");

        return Result.success();
    }
    /*
    根据id 查询菜品
    页面回显
     * @param id
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品：{}", id);
        DishVO dishVo = dishService.getByIdWithhFlavor(id);
        return Result.success(dishVo);
    }
    /*
    修改菜品
      * @param dishDTO
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info( "修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        clearCache("dish_*");

        return Result.success();
    }
    /*
    根据分类id查询菜品数据
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品数据")
    public Result<List<Dish>> list(Dish dish){
        log.info("根据分类id查询菜品数据：{}", dish.getCategoryId());
        List<Dish> list = dishService.list(dish.getCategoryId());
        return Result.success(list);
    }
    /*
    批量停售起售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("批量停售起售")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("批量停售起售：{}", status);
        dishService.startOrStop(status, id);

        clearCache("dish_*");
        return Result.success();
    }

    private void clearCache(String key){
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }



}
