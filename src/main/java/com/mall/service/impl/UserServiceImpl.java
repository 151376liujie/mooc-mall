package com.mall.service.impl;

import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.UserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: jonny
 * Time: 2017-05-11 18:36.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonResponse<User> login(String username, String password) {

        password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectUserByNameAndPass(username, password);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_OR_PASSWORD_NOT_EXISTS);
        }

        user.setPassword(StringUtils.EMPTY);

        return JsonResponseBuilder.buildSuccessJsonResponse(user);
    }

    @Override
    public JsonResponse<String> register(User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_MUST_NOT_BE_BLANK);
        }
        int userCount = userMapper.checkUsername(user.getUsername());
        if (userCount > 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_ALREADY_EXISTS);
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_EMAIL_MUST_NOT_BE_BLANK);
        }
        int emailCount = userMapper.checkEmail(user.getEmail());
        if (emailCount > 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_EMAIL_ALREADY_EXISTS);
        }
        user.setRole(Constants.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int insert = userMapper.insert(user);
        if (insert == 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(ResponseCode.OK);
    }
}
