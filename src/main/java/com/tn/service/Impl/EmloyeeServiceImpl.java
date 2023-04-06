package com.tn.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.Employee;
import com.tn.mapper.EmployeeMapper;
import com.tn.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmloyeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
