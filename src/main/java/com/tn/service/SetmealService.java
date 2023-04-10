package com.tn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tn.domain.Setmeal;
import com.tn.dto.DishDto;
import com.tn.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    void updateWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);
}
