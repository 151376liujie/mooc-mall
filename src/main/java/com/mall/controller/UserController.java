package com.mall.controller;

import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Author: jonny
 * Time: 2017-05-11 00:15.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse<User> login(String username, String password, HttpSession session) {
        JsonResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Constants.SESSION_USER_KEY, response.getData());
            return JsonResponseBuilder.buildSuccessJsonResponse(response.getData());
        }
        return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_OR_PASSWORD_NOT_EXISTS);
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonResponse logout(HttpSession session) {
        session.removeAttribute(Constants.SESSION_USER_KEY);
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResponse<String> register(User user) {
        return userService.register(user);
    }
}
