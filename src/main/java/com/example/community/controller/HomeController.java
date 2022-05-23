package com.example.community.controller;

import com.example.community.model.DiscussPost;
import com.example.community.model.Page;
import com.example.community.model.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: community
 * @description: 首页。这个controller没有指定路径
 * @author: zjx
 * @create: 2022-05-22 20:15
 **/
@Controller
public class HomeController {
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;
    //使用userService，这样查到的数据就全面了

    @GetMapping("/index") //响应的是网页，所以不需要用ResponseBody注解
    public String getIndexPage(Model model, Page page){//String是视图的名字
        // 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model (MVC之妙)
        //所以，在thymeleaf中可以直接访问Page对象中数据
        //服务器也要给page设置一点值
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");


        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        for(DiscussPost discussPost:list){
            Map<String,Object> map=new HashMap<>();
            map.put("post",discussPost);
            String userId = discussPost.getUserId();
            Integer userIdNumber = Integer.valueOf(userId);
            User userById = userService.findUserById(userIdNumber);
            map.put("user",userById);
            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }





}
