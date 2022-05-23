package com.example.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.model.DiscussPost;
import com.example.community.service.DiscussPostService;
import com.example.community.mapper.DiscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86133
* @description 针对表【discuss_post】的数据库操作Service实现
* @createDate 2022-05-22 18:23:12
*/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
    implements DiscussPostService{
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        if(userId <0){
            return null;
        }
        else{
            return discussPostMapper.selectDiscussPosts(userId, offset, limit);//返回的是userId对应的user
        }
    }
    @Override
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }



}




