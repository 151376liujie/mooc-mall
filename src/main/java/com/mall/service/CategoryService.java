package com.mall.service;

import com.mall.common.JsonResponse;
import com.mall.pojo.Category;

import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-13 23:52.
 */
public interface CategoryService {

    JsonResponse<String> addCategory(String name,int parentId);

    JsonResponse<String> updateCategoryName(Integer categoryId,String categoryName);

    JsonResponse<List<Category>> getSubCategories(Integer categoryId);

    JsonResponse<List<Integer>> getDeepSubCategories(Integer categoryId);

}
