package com.leyuna.waylocation.config;

import com.leyuna.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author pengli
 * @create 2022-02-22 15:57
 * 全局异常捕捉
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${is-error:true}")
    private boolean isError;

    @ExceptionHandler(value=Exception.class)
    public DataResponse exceptionHandler(Exception e){
        e.printStackTrace();
        if(isError){
            return DataResponse.buildFailure(e.getMessage());
        }
        return DataResponse.buildFailure();
    }
}
