package com.tn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tn.common.BaseContext;
import com.tn.common.R;
import com.tn.domain.AddressBook;
import com.tn.domain.OrderDetail;
import com.tn.domain.Orders;
import com.tn.service.AddressBookService;
import com.tn.service.OrderDetailService;
import com.tn.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户下单，插入数据到订单表和订单明细表
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 移动端分页操作，展示数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userpage(int page,int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentID());
        Page<Orders> ordersPage = orderService.page(pageInfo, wrapper);
//        Orders one = orderService.getOne(wrapper);
//
//        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrderDetail::getOrderId,one.getId());
////        List<OrderDetail> list = orderDetailService.list(queryWrapper);
//        Page<OrderDetail> orderDetailPage = orderDetailService.page(pageInfo, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 后台分页操作，展示数据
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, Timestamp beginTime,Timestamp endTime){
//        log.info(beginTime.toString());
//        log.info(endTime.toString());

        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper=new LambdaQueryWrapper<>();
        //查询订单号条件
        if(number != null){
            wrapper.like(Orders::getNumber,number);
        }
        //查询订单时间条件
        if(beginTime != null && endTime != null){
            wrapper.ge(Orders::getOrderTime,beginTime);
            wrapper.le(Orders::getOrderTime,endTime);
        }

        Page<Orders> ordersPage = orderService.page(pageInfo,wrapper);
        return R.success(ordersPage);
    }

    /**
     * 更改状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,orders.getId());
        Orders order = orderService.getOne(queryWrapper);
//        log.info(orders.getStatus().toString());
        order.setStatus(orders.getStatus());
        orderService.updateById(order);
        return R.success("修改状态成功");
    }

}
