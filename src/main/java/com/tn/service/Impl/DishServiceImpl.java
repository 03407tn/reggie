package com.tn.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.Dish;
import com.tn.domain.DishFlavor;
import com.tn.dto.DishDto;
import com.tn.mapper.DishMapper;
import com.tn.service.DishFlavorService;
import com.tn.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto){
        this.save(dishDto);

        Long dishDtoId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor list:flavors){
            list.setDishId(dishDtoId);
        }

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据菜品id查询菜品信息和口味
     * @param id
     */
    public DishDto getByIDWithFlavor(Long id) {
        Dish dish=this.getById(id);

        DishDto dishDto=new DishDto();
        //将dish对象copy到dishDto
        BeanUtils.copyProperties(dish,dishDto);
        //构造条件查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        //构造查询条件
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        //将查询到的数据放入flavors
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }


}
