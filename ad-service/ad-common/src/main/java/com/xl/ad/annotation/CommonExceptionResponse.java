package com.xl.ad.annotation;

import com.xl.ad.exception.AdException;
import com.xl.ad.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionResponse {

    @ExceptionHandler(value = AdException.class)
    public Result<String> CommonExceptionResponse(AdException ex){
        Result<String> result = new Result<>(-1,"服务器发生错误");
        result.setData(ex.getMessage());
        return result;
    }

}
