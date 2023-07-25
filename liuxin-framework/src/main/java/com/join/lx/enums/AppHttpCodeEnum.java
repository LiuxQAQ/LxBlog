package com.join.lx.enums;

/**
 *  枚举类；提高代码的复用性，便于代码的维护
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    UPLOAD_ERROR(507,"文件格式仅支持png、jpg"),
    AUTH_OR_FAILED(508,"认证或授权失败"),
    COMMENT_NOT_NULL(506,"评论内容不能为空"),
    USERNAME_NOT_NULL(509,"用户名不能为空"),
    NICKNAME_NOT_NULL(510,"昵称不能为空"),
    PASSWORD_NOT_NULL(511,"密码不能为空"),
    EMAIL_NOT_NULL(512,"邮箱不能为空"),

    NICKNAME_EXIST(514,"昵称已存在"),
    EMAIL_NOT_EXIST(516,"邮箱已存在"),

    TAG_NAME_NOT_NULL(601,"标签名不能为空"),
    TAG_REMARK_NOT_NULL(602,"标签备注不能为空"),
    DELETE_MENU_ERROR(603,"有子菜单不能删除")
    ;



    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}