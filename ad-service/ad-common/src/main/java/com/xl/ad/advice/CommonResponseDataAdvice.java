package com.xl.ad.advice;

import com.xl.ad.annotation.IgnoreResponseAdvice;
import com.xl.ad.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//...advice就是对什么进行增强，对rest响应进行增强，也就是统一响应处理
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice {

//    是否要进行统一响应处理
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        如果方法上或类上有@IgnoreResponseAdvice注解，则忽略
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)
            || methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        return true;
    }

//    在返回响应体时进行增强
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        统一返回这个响应的vo
        Result result = new Result(0,"");
        if(o == null){
            return result;
        }else if(o instanceof Result){
            result = (Result) o;
        }else {
            result.setData(o);
        }
        return result;
    }
}
