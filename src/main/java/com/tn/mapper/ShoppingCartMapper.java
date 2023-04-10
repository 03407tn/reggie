package com.tn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tn.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
