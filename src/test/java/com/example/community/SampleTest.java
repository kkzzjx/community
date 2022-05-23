package com.example.community;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.community.mapper.DiscussPostMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.DiscussPost;
import com.example.community.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-22 17:09
 **/
@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(41, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testUpdate(){

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",149);
        User user=new User();
        user.setStatus(2);
        int update = userMapper.update(user, queryWrapper);
    }

    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost d:discussPosts){
            System.out.println(d);
        }

        int rows=discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);


    }

}

