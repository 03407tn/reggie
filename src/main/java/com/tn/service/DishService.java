package com.tn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tn.domain.Dish;
import com.tn.dto.DishDto;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIDWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
