package com.example.community.controller.interceptor;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-31 11:34
 **/
@Component
public class AlphaInterceptor implements HandlerInterceptor {


    /**
     * 在controller之前执行。  返回false了就停止执行 一般返回true
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);


    }

    /**
     * 多一个modelAndView  controller执行完毕，需要给模版引擎返回内容。
     * 具体执行时机是在controller以后，模版引擎之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在程序最后执行，模版之后执行
     * @param request
     * @param response
     * @param handler 拦截的目标
     * @param ex  模版处理过程中的异常
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
