package com.tn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tn.domain.Dish;
import com.tn.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIDWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void removeWithFlavor(List<Long> ids);
}
