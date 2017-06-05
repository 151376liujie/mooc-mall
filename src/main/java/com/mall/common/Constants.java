package com.mall.common;

import com.google.common.collect.ImmutableSet;

/**
 * Author: jonny
 * Time: 2017-05-11 21:59.
 */
public class Constants {

    public static final String SESSION_USER_KEY = "key_session_user";

    public static final String SESSION_ADMIN_ROLE = "key_session_admin";

    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public interface Cart{
        /**
         * 购物车中商品的选中状态
         */
        int CHECKED = 1;
        /**
         * 购物车中商品的未选中状态
         */
        int UNCHECKED = 0;

        String LIMIT_NUM_FAILED = "LIMIT_NUM_FAILED";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface ProductOrderBy{
        ImmutableSet<String> orderBySet = ImmutableSet.copyOf(new String[]{"price_desc","price_asc"});
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }


}
