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

    ERROR_ENTITY_NOT_EXISTS(3000, "数据不存在"),

    ERROR_SERVER_ERROR(4000, "系统内部错误"),

    ERROR_USERNAME_OR_PASSWORD_NOT_EXISTS(5000, "用户名或密码错误"),

    ERROR_USERNAME_MUST_NOT_BE_BLANK(5001, "用户名不能为空"),

    ERROR_USERNAME_ALREADY_EXISTS(5002, "用户名已存在"),

    ERROR_EMAIL_MUST_NOT_BE_BLANK(5003, "邮箱不能为空"),

    ERROR_EMAIL_ALREADY_EXISTS(5004, "邮箱已存在");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
