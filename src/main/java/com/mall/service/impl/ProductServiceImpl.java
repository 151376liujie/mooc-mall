package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.enums.StatusEnum;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import com.mall.vo.ProductDetailVO;
import com.mall.vo.ProductListVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-16 12:54.
 */
@Service
public class ProductServiceImpl implements ProductService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Value("${ftp.server.http.prefix}")
    private String ftpServerPrefix;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public JsonResponse<String> saveOrUpdateProduct(Product product) {
        if (product == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        if (StringUtils.isBlank(product.getSubImages())) {
            String[] split = product.getSubImages().split(",");
            if (ArrayUtils.isEmpty(split)) {
                return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
            }
            product.setMainImage(split[0]);
        }
        if (product.getId() == null) {
            int insert = productMapper.insert(product);
            if (insert <= 0) {
                return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
            }
        } else {
            int update = productMapper.updateByPrimaryKey(product);
            if (update <= 0) {
                return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
            }
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int updateRow = productMapper.updateByPrimaryKeySelective(product);
        if (updateRow <= 0) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
        return JsonResponseBuilder.buildSuccessJsonResponseWithoutData();
    }

    @Override
    public JsonResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        try {
            BeanUtils.copyProperties(productDetailVO, product);
            productDetailVO.setImageHost(ftpServerPrefix);
            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category == null) {
                productDetailVO.setParentCategoryId(0);
            } else {
                productDetailVO.setParentCategoryId(category.getParentId());
            }
            productDetailVO.setCreateTime(DateFormatUtils.format(product.getCreateTime(), Constants.DATE_TIME_FORMAT_PATTERN));
            productDetailVO.setUpdateTime(DateFormatUtils.format(product.getUpdateTime(), Constants.DATE_TIME_FORMAT_PATTERN));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }

        return JsonResponseBuilder.buildSuccessJsonResponse(productDetailVO);
    }

    @Override
    public JsonResponse listProducts(int pageNo, int pageSize) {

        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVO> productListVOList = transferToProductListVO(productList);
        PageInfo pageInfo = new PageInfo<>(productListVOList);
        pageInfo.setList(productListVOList);
        return JsonResponseBuilder.buildSuccessJsonResponse(pageInfo);
    }

    @Override
    public JsonResponse searchProducts(Integer productId, String productName, int pageNo, int pageSize) {

        if (StringUtils.isNotBlank(productName)) {
            productName = "%" + productName + "%";
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList = productMapper.selectByIdAndName(productId, productName);
        List<ProductListVO> productListVOS = transferToProductListVO(productList);
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productListVOS);
        return JsonResponseBuilder.buildSuccessJsonResponse(pageInfo);
    }

    public JsonResponse<ProductDetailVO> getPortalProductDetail(Integer productId){
        if (productId == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }

        if (product.getStatus() != StatusEnum.ON_SALE.getCode()) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        try {
            BeanUtils.copyProperties(productDetailVO, product);
            productDetailVO.setImageHost(ftpServerPrefix);
            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category == null) {
                productDetailVO.setParentCategoryId(0);
            } else {
                productDetailVO.setParentCategoryId(category.getParentId());
            }
            productDetailVO.setCreateTime(DateFormatUtils.format(product.getCreateTime(), Constants.DATE_TIME_FORMAT_PATTERN));
            productDetailVO.setUpdateTime(DateFormatUtils.format(product.getUpdateTime(), Constants.DATE_TIME_FORMAT_PATTERN));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }

        return JsonResponseBuilder.buildSuccessJsonResponse(productDetailVO);
    }

    @Override
    public JsonResponse<PageInfo> queryProductsByKeywordAndCategoryId(String keyword, Integer categoryId, int pageNum, int pageSize,String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null){
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        List<Integer> categoryIds = null;
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOS = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOS);
                return JsonResponseBuilder.buildSuccessJsonResponse(pageInfo);
            }
            categoryIds = categoryService.getDeepSubCategories(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Constants.ProductOrderBy.orderBySet.contains(orderBy)) {
                String[] split = orderBy.split("_");
                PageHelper.orderBy(split[0] + " " + split[1]);
            }
        }
        if (StringUtils.isBlank(keyword)){
            keyword = null;
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIds);
        List<ProductListVO> productListVOS = transferToProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOS);
        return JsonResponseBuilder.buildSuccessJsonResponse(pageInfo);
    }

    private List<ProductListVO> transferToProductListVO(List<Product> productList) {
        List<ProductListVO> list = new ArrayList<>(productList.size());
        ProductListVO productListVO = null;
        try {
            for (Product product : productList) {
                productListVO = new ProductListVO();
                BeanUtils.copyProperties(productListVO, product);
                productListVO.setImageHost(ftpServerPrefix);
                list.add(productListVO);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;

    }
}
