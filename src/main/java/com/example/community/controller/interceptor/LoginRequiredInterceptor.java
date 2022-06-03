package com.example.community.controller.interceptor;

import com.example.community.annotation.LoginRequired;
import com.example.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-06-01 17:17
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;
    //尝试获取当前用户 如果存在就已经登录，如果不存在就没登录

    //Object是拦截的目标。 如果Object是我们要拦截的方法，再进行处理。 （因为还有可能拦截静态资源，所以说要先进行判断）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){ //HandlerMethod是SpringMVC提供的一个类型，表示方法类型
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            Method method=handlerMethod.getMethod(); //获取方法
            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class); //获取注解 `Method.getAnnotation(Class<T> annotationClass)`
            if(loginRequired!=null&& hostHolder.getUser()==null){ //没有登录；但方法需要访问权限，那么拦截
                response.sendRedirect(request.getContextPath()+"/login");//重定向到login页面...
            }
        }

        return true;
    }
}
