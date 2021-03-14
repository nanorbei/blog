package com.yp.blog.config;

import com.yp.blog.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* SpringBoot :mvc自动配置原理
* 扩展自动配置类
* @Configuration表明该类是一个注解类，并且类型为WebMvcConfigurer
*
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //该拦截器需要拦截/admin下除了登录页面以及登陆页面提交的请求
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin","/admin/login");
    }
}
