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


}
