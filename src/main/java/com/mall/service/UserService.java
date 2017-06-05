package com.mall.service;


import com.mall.common.JsonResponse;
import com.mall.pojo.User;

/**
 * Author: jonny
 * Time: 2017-05-11 18:35.
 */
public interface UserService {

    JsonResponse<User> login(String username, String password);

    JsonResponse<String> register(User user);

    JsonResponse<String> checkValid(String value, String type);

    JsonResponse<String> getQuestionForForgetPassword(String username);

    JsonResponse<String> checkAnswerForForgetPassword(String username,String question,String answer);

    JsonResponse<String> resetPasswordForForgetPassword(String username,String token,String newPass);

    JsonResponse<String> resetPassword(String oldPass,String newPass,User user);

    JsonResponse<User> updateUserInfo(User user);

    JsonResponse<User> getUserInfo(int userId);


    JsonResponse<String> checkAdmin(User user);
}
