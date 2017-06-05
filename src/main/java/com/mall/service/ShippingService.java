package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.common.JsonResponse;
import com.mall.pojo.Shipping;

/**
 * Author: jonny
 * Time: 2017-05-30 11:01.
 */
public interface ShippingService {

    JsonResponse add(Integer userId, Shipping shipping);

    JsonResponse del(Integer userId,Integer shippingId);

    JsonResponse update(Integer userId,Shipping shipping);

    JsonResponse select(Integer userId,Integer shippingId);

    JsonResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
