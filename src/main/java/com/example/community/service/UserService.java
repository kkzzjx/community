package com.example.community.service;

import com.example.community.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 86133
* @description 针对表【user】的数据库操作Service
* @createDate 2022-05-22 18:37:34
*/
public interface UserService extends IService<User> {

    /**
     * 通过用户id查找到用户
     * @param userId
     * @return
     */
    User findUserById(int userId);

    /**
     * 用户注册
     * @param user
     * @return 返回Map，（错误信息，错误信息提示） 如果返回的Map为空，说明注册成功
     */
    Map<String,Object> register(User user);


    /**
     * 激活 判断这个用户是否需要激活，然后操作
     * @param userId
     * @param code
     * @return
     */

    int activation(int userId,String code);


    /**
     * 登录
     * @param username
     * @param password
     * @return 返回的结果用map记录
     */
    Map<String,Object> Login(String username,String password,int expiredSeconds);

    public void logout(String ticket);

}
