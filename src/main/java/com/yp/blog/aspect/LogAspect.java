package com.yp.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//声明当前类是一个切面,将当前类交给Spring容器
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //拦截web包中的所有类中所有方法，即所有Controller中的所有方法
    @Pointcut("execution(* com.yp.blog.web.*.*(..))")
    public void log() {}
    //在切点所拦截到的连接点方法之前执行
    @Before("log()")
    public void before(JoinPoint joinPoint) {
        //拿到url与ip
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //借助切面拿到处理器类以及对应方法
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+ "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        //拿到日志
        logger.info("-------------before------------");
        logger.info("request: {}",requestLog);
    }
    //虽然后置增强方法是在被代理对象之后执行的，但是该方法无法获得被代理对象本身的执行结果。
    @After("log()")
    public void after(){
        logger.info("--------------after------------");
    }
    //@AfterReturning修饰的方法称为返回增强方法，Spring AOP会将这个方法加在被代理对象之后，它可以通过在注释中的"returning="获得被代理对象本身的执行结果。
    @AfterReturning(pointcut="log()",returning = "result")
    public void afterReturning(Object result) {
        logger.info("result : {}", result);
    }
    //定义一个内部类，封装我们需要拿到的关于请求的信息
    private class RequestLog {
        String url;
        String ip;
        String classMethod;
        Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
