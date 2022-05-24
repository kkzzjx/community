package com.example.community.controller;

import com.example.community.model.User;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @program: community
 * @description: 登录注册
 * @author: zjx
 * @create: 2022-05-23 22:52
 **/
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(User user, Model model){
        Map<String, Object> map = userService.register(user);
        if(map==null|| map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        else{
           model.addAttribute("usernameMsg",map.get("usernameMsg"));
           model.addAttribute("passwordMsg",map.get("passwordMsg"));
           model.addAttribute("emailMsg",map.get("emailMsg"));



            return "site/register";
        }

    }

}
