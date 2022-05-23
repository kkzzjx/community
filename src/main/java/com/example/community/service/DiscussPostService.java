package com.example.community.service;

import com.example.community.model.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86133
* @description 针对表【discuss_post】的数据库操作Service
* @createDate 2022-05-22 18:23:12
*/
public interface DiscussPostService extends IService<DiscussPost> {
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);
    int findDiscussPostRows(int userId);

}
