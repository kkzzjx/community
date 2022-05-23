package com.example.community.service;

import com.example.community.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86133
* @description 针对表【user】的数据库操作Service
* @createDate 2022-05-22 18:37:34
*/
public interface UserService extends IService<User> {
    User findUserById(int userId);

}
