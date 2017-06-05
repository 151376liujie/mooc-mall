package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.Shipping;
import com.mall.pojo.User;
import com.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Author: jonny
 * Time: 2017-05-30 11:01.
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {


    @Autowired
    private ShippingService shippingService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return shippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public JsonResponse del(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return shippingService.del(user.getId(), shippingId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResponse update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return shippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public JsonResponse select(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return shippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JsonResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return shippingService.list(user.getId(), pageNum, pageSize);
    }

}
