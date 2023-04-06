package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.Setmeal;
import com.tn.mapper.SetmealMapper;
import com.tn.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
}
