package com.tn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tn.domain.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
