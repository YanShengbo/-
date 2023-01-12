package com.example.usermanager.config;


import com.example.usermanager.util.ConstVariable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义用户拦截器
 */
@Component
public class LoginIntercept implements HandlerInterceptor {
    /**
     * true 表示用户已经登录，会继续访问目标方法
     * false 表示未登录，跳转到登录界面
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute(ConstVariable.USER_SEESION_KEY)!=null){
            // 表示已经登录
            return true;
        }
        //未登录，跳转到登录页面
        response.sendRedirect("/login.html");
        return false;
    }
}
