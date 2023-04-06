package com.tn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tn.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
