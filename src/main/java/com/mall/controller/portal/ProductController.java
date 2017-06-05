package com.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mall.common.JsonResponse;
import com.mall.service.ProductService;
import com.mall.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: jonny
 * Time: 2017-05-27 11:25.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResponse<ProductDetailVO> getProductDetail(Integer productId) {
        JsonResponse<ProductDetailVO> portalProductDetail = productService.getPortalProductDetail(productId);
        return portalProductDetail;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "orderBy", required = false) String orderBy) {
        return productService.queryProductsByKeywordAndCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
