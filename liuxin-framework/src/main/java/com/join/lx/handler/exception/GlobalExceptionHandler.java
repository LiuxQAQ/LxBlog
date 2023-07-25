package com.join.lx.handler.exception;


import com.join.lx.domain.ResponseResult;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 对 SystemException 异常处理
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        // 打印异常信息
        log.error("出现了异常! {}",e);
        // 从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    // 对 其他 异常处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        // 打印异常信息
        log.error("出现了异常! {}",e);
        // 从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
