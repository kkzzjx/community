package com.example.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 86133
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-05-22 18:37:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    UserMapper userMapper;

    @Override
    public User findUserById(int userId) {

        return userMapper.selectById(userId);
    }
}




