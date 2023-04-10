package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.OrderDetail;
import com.tn.mapper.OrderDetailMapper;
import com.tn.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
