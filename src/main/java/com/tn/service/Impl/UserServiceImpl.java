package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.User;
import com.tn.mapper.UserMapper;
import com.tn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
