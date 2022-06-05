package com.example.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.model.DiscussPost;
import com.example.community.service.DiscussPostService;
import com.example.community.mapper.DiscussPostMapper;
import com.example.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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
    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    @Override
    public int addDiscussPost(DiscussPost post) {
        if(post==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义HTML标记  使用HtmlUtils工具即可！！ 防止xss嘛
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insert(post);

    }


}




