package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.DishFlavor;
import com.tn.mapper.DishFlavorMapper;
import com.tn.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
