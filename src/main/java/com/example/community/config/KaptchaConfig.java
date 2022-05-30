package com.example.community.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-25 19:35
 **/
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.image.width","100");
        properties.put("kaptcha.image.height","40");
        properties.put("kaptcha.textproducer.font.size", "32");
        properties.put("kaptcha.textproducer.font.color", "0,0,0");
        properties.put("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.put("kaptcha.textproducer.char.length", "4");
      // properties.put("kaptcha.textproducer.font.names", "Arial,Courier,cmr10,宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }





}
