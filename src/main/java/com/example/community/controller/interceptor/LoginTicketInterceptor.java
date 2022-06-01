package com.example.community.controller.interceptor;

import com.example.community.model.LoginTicket;
import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.utils.CookieUtil;
import com.example.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: community
 * @description: 从cookie中获取存的ticket，服务端通过ticket获知用户，然后用模版将用户显示
 * @author: zjx
 * @create: 2022-05-31 16:24
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
       if(ticket!=null){
           LoginTicket loginTicket=userService.findLoginTicket(ticket);
           //查看这个ticket是否在有效期内
           if(loginTicket!=null&&loginTicket.getExpired().after(new Date())){
               //根据凭证查询用户
               User user = userService.findUserById(loginTicket.getUserId());
               //在本次请求中查询用户 要考虑多线程的情况（不能是存在容器之类的 只存一个
               //隔离存 存在ThreadLocal里
               hostHolder.setUser(user);
           }
       }
        return true;
    }

    /**
     * post这个方法是在模版引擎前调用的，而模版引擎需要使用这个user
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
        //模版可以用user
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
