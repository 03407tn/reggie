package com.tn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.tn.common.R;
import com.tn.domain.Category;
import com.tn.domain.Dish;
import com.tn.domain.DishFlavor;
import com.tn.dto.DishDto;
import com.tn.service.CategoryService;
import com.tn.service.DishFlavorService;
import com.tn.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        //全部清理
        Set keys= redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        //精确清理
//        String key="dish_" + dishDto.getCategoryId() +"_1";
//        redisTemplate.delete(key);
        return R.success("添加菜品成功");

    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();


        List<DishDto> list=records.stream().map((item) -> {

            DishDto dishDto=new DishDto();
            Category category = categoryService.getById(item.getCategoryId());
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            BeanUtils.copyProperties(item,dishDto);

            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据菜品id查询菜品信息和口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIDWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        //全部清理
//        Set keys= redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        //精确清理
        String key="dish_" + dishDto.getCategoryId() +"_1";
        redisTemplate.delete(key);

        return R.success("修改菜品成功");
    }

    /**
     * 删除或批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        dishService.removeWithFlavor(ids);
        return R.success("删除菜品成功");
    }

    /**
     * 修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        log.info(ids.toString());
        log.info(status.toString());
        for(Long id:ids){
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
            //精确清理
            String key="dish_" + ids +"_1";
            redisTemplate.delete(key);
        }

        //全部清理
//        Set keys= redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        return R.success("修改状态成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> dish(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> dish(Dish dish){

        List<DishDto> dtoList=null;

        String key="dish_" +dish.getCategoryId()+"_"+dish.getStatus();

        //获取redis缓存数据
        dtoList=(List<DishDto>) redisTemplate.opsForValue().get(key);

        //如果缓存中有数据，直接返回
        if(dtoList != null){
            return R.success(dtoList);
        }
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dtoList=list.stream().map((item)->{

            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        //首次访问添加数据到redis
        redisTemplate.opsForValue().set(key,dtoList,60, TimeUnit.MINUTES);
        return R.success(dtoList);
    }
}
