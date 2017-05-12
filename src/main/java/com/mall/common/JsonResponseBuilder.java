package com.mall.common;

/**
 * description:
 * <p/>
 * author:刘杰
 * time: 2017-04-14 10:13.
 * package_name : com.doubao.finance.util
 */
public class JsonResponseBuilder {

    public static <T> JsonResponse buildJsonResponse(ResponseCode code, T data) {
        if (code == null) {
            throw new NullPointerException();
        }
        if (data == null) {
            return new JsonResponse(code.getCode(), code.getMessage(), "");
        }
        return new JsonResponse(code.getCode(), code.getMessage(), data);
    }

    public static <T> JsonResponse buildSuccessJsonResponse(T data) {
        return buildJsonResponse(ResponseCode.OK, data);
    }

    public static JsonResponse buildSuccessJsonResponseWithoutData() {
        return buildSuccessJsonResponse(null);
    }

    public static <T> JsonResponse buildErrorJsonResponse(ResponseCode code, T data) {
        if (code == null) {
            throw new NullPointerException();
        }
        if (code == ResponseCode.OK) {
            throw new IllegalArgumentException("build error json code ,but you passed a success json code ！");
        }
        return buildJsonResponse(code, data);
    }

    public static JsonResponse buildErrorJsonResponseWithoutData(ResponseCode code) {
        return buildErrorJsonResponse(code, null);
    }

}
