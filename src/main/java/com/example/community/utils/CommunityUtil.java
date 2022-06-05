package com.example.community.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
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




    /**
     * 服务器向浏览器返回的json内容往往会包含几部分 将这些部分封装在一起
     * @param code 服务器向服务器返回的编码
     * @param msg 返回的提示信息
     * @param map 返回的业务数据
     * @return JSON格式的字符串
     */

    public static String getJSONString(int code, String msg, Map<String,Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    /**
     * 方法重载， msg不一定有
     * @param code
     * @param msg
     * @return
     */
    public static String getJSONString(int code, String msg) {
        return getJSONString(code,msg,null);
    }

    /**
     * 方法重载 ，msg和业务数据不一定有
     * @param code
     * @return
     */
    public static String getJSONString(int code) {
        return getJSONString(code,null,null);
    }





}
