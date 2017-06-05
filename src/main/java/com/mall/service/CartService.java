package com.mall.service;

import com.mall.common.JsonResponse;

/**
 * Author: jonny
 * Time: 2017-05-28 10:26.
 */
public interface CartService {

    JsonResponse addProductToCart(Integer userId, Integer productId, Integer count);

    JsonResponse updateCart(Integer userId, Integer productId, Integer count);

    JsonResponse deleteProduct(Integer userId, String productIds);

    JsonResponse listProduct(Integer userId);

    JsonResponse selectOrUnselect(Integer userId, Integer productId, Integer check);

    JsonResponse countProduct(Integer userId);
}
