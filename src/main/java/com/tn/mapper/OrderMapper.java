package com.tn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tn.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
