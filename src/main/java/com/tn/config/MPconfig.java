package com.tn.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MPconfig {
    @Bean
    public MybatisPlusInterceptor mpInterceptor(){
		//1.定Mp拦截器
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
		//12.添加具体的拦截器
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }
}
