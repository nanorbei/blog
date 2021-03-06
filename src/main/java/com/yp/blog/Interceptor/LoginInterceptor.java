package com.yp.blog.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截到请求
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");//对应于我们的登录页面
            return false;
        }
        return true;
    }

}
