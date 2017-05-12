package com.mall.common;

/**
 * description:
 * 封装返回json格式的相应内容
 * <p/>
 * author:刘杰
 * time: 2017-04-14 10:05.
 * package_name : com.doubao.finance.util
 */
public class JsonResponse<T> extends BaseModel {

    private int code;
    private String message;
    private boolean success;
    private T data;

    public JsonResponse() {
    }

    public JsonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        setSuccess(this.code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        setSuccess(this.code);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    private void setSuccess(int code) {
        this.success = (code == ResponseCode.OK.getCode());
    }
}
