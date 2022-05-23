package com.example.community.mapper;

import com.example.community.model.DiscussPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86133
* @description 针对表【discuss_post】的数据库操作Mapper
* @createDate 2022-05-22 18:23:12
* @Entity com.example.community.model.DiscussPost
*/
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    int selectDiscussPostRows(@Param("userId")int userId);
    // 当方法只有一个参数时，比且该参数应用在<if ...>标签 上时，就必须用@Param注解为该参数取别名；if标签里面不需要#{}
}




