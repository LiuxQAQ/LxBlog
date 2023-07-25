package com.join.lx.exception;

import com.join.lx.enums.AppHttpCodeEnum;

/**
 *  自定义异常处理类
 */
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /*
        自定义异常处理类，可以将状态码参数传递
     */
    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
