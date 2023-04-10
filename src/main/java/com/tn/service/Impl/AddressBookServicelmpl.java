package com.tn.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tn.domain.AddressBook;
import com.tn.mapper.AddressBookMapper;
import com.tn.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressBookServicelmpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService
{
}
