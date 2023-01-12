package com.example.usermanager.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * 统一异常拦截处理类
 */
@RestControllerAdvice
public class MyExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public Object exceptionAdvice(Exception e){
        HashMap<String,Object> result = new HashMap<>();
        result.put("state",-1);
        result.put("msg","程序出现异常："+e.getMessage());
        result.put("data","");
        return result;
    }
}
