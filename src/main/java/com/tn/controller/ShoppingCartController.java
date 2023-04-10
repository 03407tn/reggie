package com.tn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tn.common.BaseContext;
import com.tn.common.R;
import com.tn.domain.ShoppingCart;
import com.tn.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSOutput;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 往购物车添加数据
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id,指定当前是哪个用户的购物车数据
        Long currentID = BaseContext.getCurrentID();
        shoppingCart.setUserId(currentID);

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentID);

        Long dishId = shoppingCart.getDishId();
        if(dishId != null){
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        //如果已经存在，就在原来数量基础上加一
        if(cart != null){
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        }else {
            //如果不存在，则添加到购物车，数量默认就是一
            cart=shoppingCart;
            cart.setNumber(1);
            shoppingCartService.save(cart);
        }

        return R.success(cart);
    }

    /**
     * 减少份数
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long currentID = BaseContext.getCurrentID();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        //确认当前是哪位用户操作
        queryWrapper.eq(ShoppingCart::getUserId,currentID);
        if(dishId != null){
            //减少的是菜品份数
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //减少的是套餐份数
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        Integer number = cart.getNumber();
        if(number >= 1){
            cart.setNumber(number - 1);
        }
        shoppingCartService.updateById(cart);
        return R.success(cart);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentID());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        //遍历每一条菜品和套餐数据
        for(ShoppingCart cartList:list){
            Integer number = cartList.getNumber();
            if(number == 0){
                shoppingCartService.removeById(cartList);
                List<ShoppingCart> endlist = shoppingCartService.list(queryWrapper);
                return R.success(endlist);
            }
        }
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> delete(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentID());
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车清空成功");
    }
}
