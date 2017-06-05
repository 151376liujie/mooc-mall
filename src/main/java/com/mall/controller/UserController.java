package com.mall.controller;

import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.User;
import com.mall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: jonny
 * Time: 2017-05-11 00:15.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse<User> login(String username, String password, HttpSession session, HttpServletRequest request) {
        JsonResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Constants.SESSION_USER_KEY, response.getData());
            return JsonResponseBuilder.buildSuccessJsonResponse(response.getData());
        }
        return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USERNAME_OR_PASSWORD_NOT_EXISTS);
    }


    /**
     * 用户登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JsonResponse logout(HttpSession session) {
        session.removeAttribute(Constants.SESSION_USER_KEY);
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    /**
     * 用户输入验证
     * @param value
     * @param type
     * @return
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public JsonResponse<String> checkValidate(@RequestParam(name = "val") String value, @RequestParam(name = "type") String type) {
        return userService.checkValid(value, type);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResponse<String> register(User user) {
        return userService.register(user);
    }

    /**
     * 用户登录状态下的用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        user.setPassword(StringUtils.EMPTY);
        return JsonResponseBuilder.buildSuccessJsonResponse(user);
    }

    /**
     * 忘记密码，获取用户密保问题
     * @param username
     * @return
     */
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public JsonResponse<String> getQuestionForForgetPassword(String username) {
        return userService.getQuestionForForgetPassword(username);
    }

    /**
     * 忘记密码，验证密保问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "/checkAnswer", method = RequestMethod.POST)
    public JsonResponse<String> checkAnswer(String username, String question, String answer) {
        return userService.checkAnswerForForgetPassword(username, question, answer);
    }

    /**
     * 忘记密码后重置密码
     * @param username
     * @param token
     * @param newPass
     * @return
     */
    @RequestMapping(value = "/forget/resetPassword", method = RequestMethod.POST)
    public JsonResponse<String> resetPasswordForForgetPassword(String username, String token, String newPass) {
        return userService.resetPasswordForForgetPassword(username, token, newPass);
    }

    /**
     * 用户在登录状态是修改密码
     * @param session
     * @param oldPass
     * @param newPass
     * @return
     */
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public JsonResponse<String> resetPassword(HttpSession session, String oldPass, String newPass) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<String> jsonResponse = userService.resetPassword(oldPass, newPass, user);
        return jsonResponse;
    }

    /**
     * 用户更新个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateUser",method = RequestMethod.POST)
    public JsonResponse<User> updateUserInfo(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (currentUser == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        JsonResponse<User> response = userService.updateUserInfo(user);
        if (response.isSuccess()){
            session.setAttribute(Constants.SESSION_USER_KEY,response.getData());
        }
        return response;
    }

    /**
     * 获取用户详情
     * @param session
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public JsonResponse<User> getUserDetailInfo(HttpSession session){
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (currentUser == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<User> userInfo = userService.getUserInfo(currentUser.getId());
        return userInfo;
    }





}
