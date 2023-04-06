package com.tn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tn.domain.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
