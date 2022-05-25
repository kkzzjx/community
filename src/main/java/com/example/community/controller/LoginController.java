package com.example.community.controller;

import com.example.community.model.User;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

import static com.example.community.utils.CommunityConstant.ACTIVATION_REPEAT;
import static com.example.community.utils.CommunityConstant.ACTIVATION_SUCCESS;

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

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(User user, Model model){
        Map<String, Object> map = userService.register(user);
        if(map==null|| map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封邮件，请尽快激活！");
            model.addAttribute("target","index");
            return "/site/operate-result";
        }
        else{
           model.addAttribute("usernameMsg",map.get("usernameMsg"));
           model.addAttribute("passwordMsg",map.get("passwordMsg"));
           model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }

    }
    // http://localhost:8080/activation/101/code
    @GetMapping("/activation/{userId}/{code}")  //查询请求，用get
    public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code")String code){
        int activation = userService.activation(userId, code);
        if(activation==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了");
            model.addAttribute("target","/login");
        }
        else if(activation==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活");
            model.addAttribute("target","/login");
        }
        else{
            model.addAttribute("msg","激活失败,您提供的激活码不正确");
            model.addAttribute("target","/index");
        }

        return "/site/operate-result";
    }



}
