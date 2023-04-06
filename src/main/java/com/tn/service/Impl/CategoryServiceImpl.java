package com.tn.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.common.CustomException;
import com.tn.domain.Category;
import com.tn.domain.Dish;
import com.tn.domain.Setmeal;
import com.tn.mapper.CategoryMapper;
import com.tn.service.CategoryService;
import com.tn.service.DishService;
import com.tn.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService  {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID做删除判断
     * @param id
     */

    @Override
    public void remove(Long id) {

        //添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        int count1=dishService.count(dishLambdaQueryWrapper);
        if( count1 >0){
            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        int count2=setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            //已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);

    }
}
