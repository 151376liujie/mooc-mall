package com.mall.service.impl;

import com.mall.common.*;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.UserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Author: jonny
 * Time: 2017-05-11 18:36.
 */
@Service
public class UserServiceImpl implements UserService {

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonResponse<User> login(String username, String password) {

        try {
            password = MD5Util.MD5EncodeUtf8(password);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }

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

        if (StringUtils.isBlank(user.getEmail())) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_EMAIL_MUST_NOT_BE_BLANK);
        }

        JsonResponse<String> response = checkValid(user.getUsername(), USERNAME);
        if (!response.isSuccess()) {
            return response;
        }

        response = checkValid(user.getEmail(), EMAIL);
        if (!response.isSuccess()) {
            return response;
        }

        user.setRole(Constants.Role.ROLE_CUSTOMER);
        try {
            user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }

        int insert = userMapper.insert(user);
        if (insert <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(ResponseCode.OK);
    }

    @Override
    public JsonResponse<String> checkValid(String value, String type) {
        if (StringUtils.isBlank(type)) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        if (EMAIL.equalsIgnoreCase(type)) {
            int checkEmail = userMapper.checkEmail(value);
            if (checkEmail > 0) {
                return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_EMAIL_ALREADY_EXISTS);
            }
        } else if (USERNAME.equalsIgnoreCase(type)) {
            int userCount = userMapper.checkUsername(value);
            if (userCount > 0) {
                return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_ALREADY_EXISTS);
            }
        }

        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<String> getQuestionForForgetPassword(String username) {
        JsonResponse<String> response = this.checkValid(username, USERNAME);
        if (response.isSuccess()) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_NOT_EXISTS);
        }
        String question = userMapper.selectQuestion(username);
        if (StringUtils.isBlank(question)) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_SET_QUESTION);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(question);
    }

    @Override
    public JsonResponse<String> checkAnswerForForgetPassword(String username, String question, String answer) {
        JsonResponse<String> response = this.checkValid(username, USERNAME);
        if (response.isSuccess()) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_NOT_EXISTS);
        }
        int count = userMapper.checkAnswer(username, question, answer);
        if (count <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_ANSWER_ERROR);
        }
        String token = UUID.randomUUID().toString();
        TokenCache.setKey(TOKEN_PREFIX + username, token);
        return JsonResponseBuilder.buildSuccessJsonResponse(token);
    }

    @Override
    public JsonResponse<String> resetPasswordForForgetPassword(String username, String token, String newPass) {
        JsonResponse<String> response = this.checkValid(username, USERNAME);
        if (response.isSuccess()) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_NOT_EXISTS);
        }
        if (!StringUtils.equals(token, TokenCache.getValue(TOKEN_PREFIX + username))) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_RESET_PASSWORD_TOKEN_EXPIRED);
        }
        String md5Pass = null;
        try {
            md5Pass = MD5Util.MD5EncodeUtf8(newPass);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }
        int count = userMapper.updateUserPassword(username, md5Pass);
        if (count <=0 ){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();

    }

    @Override
    public JsonResponse<String> resetPassword(String oldPass, String newPass, User user) {

        int count = 0;
        try {
            count = userMapper.checkUserPassword(MD5Util.MD5EncodeUtf8(oldPass), user.getId());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }
        if(count <= 0){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_PASSWORD_NOT_CORRECT);
        }

        try {
            user.setPassword(MD5Util.MD5EncodeUtf8(newPass));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(),e);
        }

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount <= 0){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_RESET_PASSWORD_FAILED);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<User> updateUserInfo(User user) {

        int count = userMapper.checkUserEmail(user.getEmail(), user.getId());
        if (count >0){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_EMAIL_ALREADY_BE_USED);
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount <= 0){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(updateUser);
    }

    @Override
    public JsonResponse<User> getUserInfo(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_EXISTS);
        }
        user.setPassword(StringUtils.EMPTY);
        return JsonResponseBuilder.buildSuccessJsonResponse(user);

    }

    @Override
    public JsonResponse<String> checkAdmin(User user) {
        if (user != null && user.getRole().intValue() == Constants.Role.ROLE_ADMIN){
            return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
        }
        return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PERMISSION_DENIED);
    }
}
