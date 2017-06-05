package com.mall.controller.portal;

import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.User;
import com.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Author: jonny
 * Time: 2017-05-28 10:25.
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse add(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.addProductToCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResponse update(HttpSession session, Integer productId, Integer count) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.updateCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonResponse delete(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.listProduct(user.getId());
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    public JsonResponse selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), null, Constants.Cart.CHECKED);
    }

    @RequestMapping(value = "/unSelectAll", method = RequestMethod.POST)
    public JsonResponse unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), null, Constants.Cart.UNCHECKED);
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public JsonResponse select(HttpSession session, Integer productId, Integer checkStatus) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        return cartService.selectOrUnselect(user.getId(), productId, checkStatus);
    }

    @RequestMapping(value = "/product/count", method = RequestMethod.GET)
    public JsonResponse countProduct(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildSuccessJsonResponse(0);
        }
        return cartService.countProduct(user.getId());
    }
}
