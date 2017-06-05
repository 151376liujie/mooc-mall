package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-30 11:02.
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public JsonResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.insert(shipping);
        if (count > 0) {
            HashMap<Object, Object> hashMap = Maps.newHashMap();
            hashMap.put("shippingId", shipping.getId());
            return JsonResponseBuilder.buildSuccessJsonResponse(hashMap);
        }

        return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
    }

    @Override
    public JsonResponse del(Integer userId, Integer shippingId) {
        int count = shippingMapper.selectCountByUserIdAndPrimaryKey(userId, shippingId);
        if (count <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }
        int delCount = shippingMapper.deleteByPrimaryKey(shippingId);
        if (delCount > 0) {
            return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
        }
        return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
    }

    @Override
    public JsonResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.selectCountByUserIdAndPrimaryKey(userId, shipping.getId());
        if (count <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }
        int updateRow = shippingMapper.updateByIdAndUserId(userId, shipping);
        if (updateRow <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByIdAndUserId(shippingId, userId);
        if (shipping == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }
        return JsonResponseBuilder.buildSuccessJsonResponse(shipping);
    }

    @Override
    public JsonResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return JsonResponseBuilder.buildSuccessJsonResponse(pageInfo);
    }

}
