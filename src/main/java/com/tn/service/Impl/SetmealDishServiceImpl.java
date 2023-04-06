package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.SetmealDish;
import com.tn.mapper.SetmealDishMapper;
import com.tn.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
