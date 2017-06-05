package com.mall.controller.backend;

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
 * Time: 2017-05-13 18:11.
 */

@RequestMapping("/mgr/user")
@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse<User> login(String username, String password, HttpSession session) {
        JsonResponse<User> userJsonResponse = userService.login(username, password);
        if (!userJsonResponse.isSuccess()) {
            return userJsonResponse;
        }
        User user = userJsonResponse.getData();
        if (user.getRole() != Constants.Role.ROLE_ADMIN) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PERMISSION_DENIED);
        }
        session.setAttribute(Constants.SESSION_USER_KEY, user);
        return userJsonResponse;
    }

}
