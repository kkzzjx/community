package com.example.community.config;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-22 17:20
 **/

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import org.springframework.context.annotation.Bean;

public class config {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }
}