package com.mall.controller.backend;

import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.User;
import com.mall.service.CategoryService;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-13 23:14.
 */
@RestController
@RequestMapping(value = "/mgr/category")
public class CategoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public JsonResponse<String> addCategory(HttpSession session, String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()){
            return checkAdmin;
        }

        return categoryService.addCategory(categoryName,parentId);
    }

    @RequestMapping(value = "/updateName",method = RequestMethod.POST)
    public JsonResponse<String> updateCategoryName(HttpSession session, String categoryName,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()){
            return checkAdmin;
        }

        return categoryService.updateCategoryName(categoryId,categoryName);
    }

    @RequestMapping(value = "/subCategories",method = RequestMethod.GET)
    public JsonResponse getSubCategories(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()){
            return checkAdmin;
        }
        return categoryService.getSubCategories(categoryId);
    }

    @RequestMapping(value = "/deep/subCategories",method = RequestMethod.GET)
    public JsonResponse getSubCategoriesForDeep(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }
        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()){
            return checkAdmin;
        }
        JsonResponse<List<Integer>> deepSubCategories = categoryService.getDeepSubCategories(categoryId);
        return deepSubCategories;
    }

}
