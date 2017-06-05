package com.mall.enums;

/**
 * Author: jonny
 * Time: 2017-05-27 11:30.
 */
public enum StatusEnum {

    //1-在售 2-下架 3-删除',

    ON_SALE(1),
    OFF_SALE(2),
    DELETED(3);

    private final int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
