package com.example.community.controller;

import com.example.community.model.DiscussPost;
import com.example.community.model.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.UserService;
import com.example.community.utils.CommunityUtil;
import com.example.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-06-05 10:33
 **/
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        //需要用户先登录
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONString(403,"还没有登录");
        }

        DiscussPost discussPost=new DiscussPost();
        discussPost.setUserId(user.getId().toString());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        //报错的情况将来统一处理
        return CommunityUtil.getJSONString(0,"发布成功");
    }

    // 查看帖子详情
    // 先要在帖子列表的标题上加上url，使其能够访问到这个链接
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId")String discussPostId, Model model){
        // 从数据库中查
        //帖子
        DiscussPost discussPost=discussPostService.getById(discussPostId);
        model.addAttribute("discussPost",discussPost);
        //作者
        User user=userService.getById(discussPost.getUserId());
        model.addAttribute("user",user);
        return "site/discuss-detail";


    }

}
