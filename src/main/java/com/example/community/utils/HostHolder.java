package com.example.community.utils;

import com.example.community.model.User;
import org.springframework.stereotype.Component;

/**
 * @program: community
 * @description: 容器 持有用户的信息，用于代替session对象
 * @author: zjx
 * @create: 2022-05-31 16:38
 **/
@Component
public class HostHolder {
    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }
    //清理 请求结束是清理user
    public void clear(){
        users.remove();
    }

}
