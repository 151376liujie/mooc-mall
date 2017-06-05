package com.mall.common;

/**
 * description:
 * 响应码的封装
 * <p/>
 * author:刘杰
 * time: 2017-04-14 10:19.
 * package_name : com.doubao.finance.util
 */
public enum ResponseCode {

    OK(1000, "OK"),

    ERROR_PARAMETER(2000, "参数缺失或非法"),

    ERROR_PERMISSION_DENIED(2001,"无访问权限"),

    ERROR_ENTITY_NOT_EXISTS(3000, "数据不存在"),

    ERROR_SERVER_ERROR(4000, "系统内部错误"),

    ERROR_USERNAME_OR_PASSWORD_NOT_EXISTS(5000, "用户名或密码错误"),

    ERROR_USERNAME_MUST_NOT_BE_BLANK(5001, "用户名不能为空"),

    ERROR_USERNAME_ALREADY_EXISTS(5002, "用户名已存在"),

    ERROR_EMAIL_MUST_NOT_BE_BLANK(5003, "邮箱不能为空"),

    ERROR_EMAIL_ALREADY_EXISTS(5004, "邮箱已存在"),

    ERROR_USER_NOT_LOGIN(5005,"用户未登录"),

    ERROR_USERNAME_NOT_EXISTS(5006, "用户名不存在"),

    ERROR_USER_NOT_SET_QUESTION(5007,"用户未设置密保问题"),

    ERROR_USER_ANSWER_ERROR(5008,"用户密保问题回答错误"),

    ERROR_RESET_PASSWORD_TOKEN_EXPIRED(5009,"token过期"),

    ERROR_USER_PASSWORD_NOT_CORRECT(5010,"用户密码错误"),

    ERROR_USER_RESET_PASSWORD_FAILED(5011,"用户重置密码失败"),

    ERROR_USER_EMAIL_ALREADY_BE_USED(5012,"邮箱已被占用"),

    ERROR_USER_NOT_EXISTS(5013, "用户不存在");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
