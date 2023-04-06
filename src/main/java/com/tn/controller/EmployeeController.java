package com.tn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tn.common.R;
import com.tn.domain.Employee;
import com.tn.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //    1、将页面提交的密码password进行md5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //    2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWapper = new LambdaQueryWrapper<>();
        queryWapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWapper);
//        System.out.println(emp.getPassword());
        //    3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //    4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //    5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");

        }

        //    6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        log.info("新增数据==>{}",employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateTime(LocalDateTime.now());
//
//        Long empID= (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
//        log.info("page= {},pageSize={},name={}",page,pageSize,name);

        //构造分页查询对象
        Page pageinfo =new Page(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Employee> queryWrapper= new LambdaQueryWrapper<Employee>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //按时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageinfo,queryWrapper);
        // 返回结果

        return R.success(pageinfo);
    }

    /**
     * 员工信息修改
     * @param employee
     * @param request
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){

//        Long empID;
//        empID=(Long) request.getSession().getAttribute("employee");
//
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empID);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 员工信息查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getByID(@PathVariable Long id){

        Employee employee= employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("查询员工信息为空");
    }
}
