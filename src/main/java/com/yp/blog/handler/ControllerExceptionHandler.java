package com.yp.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice注解表示会对类上标注@Controller注解的类中的方法进行拦截
@ControllerAdvice
public class ControllerExceptionHandler {
    //我们可以将错误信息保存在日志中 控制台打印 使用日志文件进行保存 注意这个日志是slf4j的哦
    Logger logger = LoggerFactory.getLogger(this.getClass());
    //@ExceptionHandler(Exception.class)该方法会对拦截到的方法进行判断，判断是否是Exception异常以及子异常，如果是的话，该方法就会执行
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.addObject("exception",e);
        modelAndView.setViewName("error/error");
        //这个是logger中error方法的使用 可以使用占位符{}
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(),e);
        return modelAndView;

    }
    //@ExceptionHandler(ArithmeticException.class)该方法会对拦截到的方法进行判断，判断是否是ArithmeticException异常以及子异常，如果是的话，该方法就会执行
    /*@ExceptionHandler(ArithmeticException.class)
    public ModelAndView exceptionHandler2(HttpServletRequest request,Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.addObject("exception",e);
        modelAndView.setViewName("error/error");
        return modelAndView;

    }*/


}
