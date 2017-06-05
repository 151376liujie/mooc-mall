package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: jonny
 * Time: 2017-05-14 09:05.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public JsonResponse<String> addCategory(String name, int parentId) {

        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setStatus(true);
        int count = categoryMapper.insert(category);
        if (count <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<String> updateCategoryName(Integer categoryId, String categoryName) {

        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<List<Category>> getSubCategories(Integer categoryId) {

        List<Category> subCategories = categoryMapper.getSubCategoriesByParentId(categoryId);
        if (CollectionUtils.isEmpty(subCategories)) {
            LOGGER.warn("未找到该分类下的子分类，categoryId:{}", categoryId);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(subCategories);
    }

    @Override
    public JsonResponse<List<Integer>> getDeepSubCategories(Integer categoryId) {
        List<Integer> list = Lists.newArrayList();
        Set<Category> resultSet = new HashSet<>();
        Set<Category> categories = listDeepSubCategories(resultSet, categoryId);
        for (Category category : categories) {
            list.add(category.getId());
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(list);
    }

    private Set<Category> listDeepSubCategories(Set<Category> set, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            set.add(category);
        }
        List<Category> categories = categoryMapper.getSubCategoriesByParentId(categoryId);
        for (Category categoryItem : categories) {
            this.listDeepSubCategories(set,categoryItem.getId());
        }
        return set;
    }
}
