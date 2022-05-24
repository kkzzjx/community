package com.example.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @program: community
 * @description: 这里没有用容器托管（也就是无component
 * @author: zjx
 * @create: 2022-05-23 23:14
 **/
public class CommunityUtil {
    //生成随机字符串
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    public static String getMd5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }else{
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }

    }
}
