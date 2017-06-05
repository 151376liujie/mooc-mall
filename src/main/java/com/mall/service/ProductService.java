package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.JsonResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVO;

/**
 * Author: jonny
 * Time: 2017-05-16 12:53.
 */
public interface ProductService {

    JsonResponse<String> saveOrUpdateProduct(Product product);

    JsonResponse<String> setSaleStatus(Integer productId,Integer status);

    JsonResponse<ProductDetailVO> getProductDetail(Integer productId);

    JsonResponse<PageInfo> listProducts(int pageNo, int pageSize);

    JsonResponse searchProducts(Integer productId, String productName, int pageNo, int pageSize);

    JsonResponse<ProductDetailVO> getPortalProductDetail(Integer productId);

    JsonResponse<PageInfo> queryProductsByKeywordAndCategoryId(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
