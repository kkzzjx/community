package com.example.community.controller;

import com.example.community.utils.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-25 15:48
 **/
@Controller
public class TestController {
    @GetMapping("/cookie/set")
    @ResponseBody
    String setCookie(HttpServletResponse response){
        Cookie cookie=new Cookie("code", CommunityUtil.getUUID());

        //设置cookie生效的范围，需要在该目录or子目录下
        cookie.setPath("/community/alpha");
        //cookie生存时间
        cookie.setMaxAge(60*10);

        //发送cookies
        response.addCookie(cookie);
        return "set cookie";
    }

    @GetMapping("/cookie/get")
    @ResponseBody
    String getCookie(@CookieValue("code") String code){ //获得这个cookie值
        System.out.println(code);
        return "get cookie";
    }

    //Session示例
    @GetMapping("/session/set")
    @ResponseBody
    String setSession(HttpSession session){  //默认path是项目目录下
        session.setAttribute("id",1);
        session.setAttribute("name","zjx");
        return "set session";
    }

    @GetMapping("/session/get")
    @ResponseBody
    String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    //分布式部署用session会有问题
    /**
     * 负载均衡 nginx发送请求
     *
     */
}
