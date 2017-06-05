package com.mall.controller.backend;

import com.google.common.collect.Maps;
import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.FileService;
import com.mall.service.ProductService;
import com.mall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Author: jonny
 * Time: 2017-05-16 12:48.
 */
@RestController
@RequestMapping("/mgr/product")
public class ProductManagerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${ftp.server.http.prefix}")
    private String uploadFilePrefix;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductManagerController.class);

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse<String> saveProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()) {
            return checkAdmin;
        }
        return productService.saveOrUpdateProduct(product);
    }


    @RequestMapping(value = "/sale/status/update", method = RequestMethod.POST)
    public JsonResponse<String> updateProductSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()) {
            return checkAdmin;
        }

        return productService.setSaleStatus(productId, status);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResponse getProductDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()) {
            return checkAdmin;
        }

        return productService.getProductDetail(productId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getProductList(HttpSession session,
                                       @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()) {
            return checkAdmin;
        }
        return productService.listProducts(pageNo, pageSize);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JsonResponse searchProduct(HttpSession session,
                                      @RequestParam(value = "productId",required = false) Integer productId,
                                      @RequestParam(value = "productName",required = false) String productName,
                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdmin = userService.checkAdmin(user);
        if (!checkAdmin.isSuccess()) {
            return checkAdmin;
        }
        return productService.searchProducts(productId, productName, pageNo, pageSize);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResponse upload(@RequestParam(name = "file") MultipartFile multipartFile, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_LOGIN);
        }

        JsonResponse<String> checkAdminResponse = userService.checkAdmin(user);
        if (!checkAdminResponse.isSuccess()) {
            return checkAdminResponse;
        }
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            String uploadPath = fileService.upload(multipartFile, path);
            Map<String, String> map = Maps.newHashMap();
            map.put("uri", uploadPath);
            map.put("url", uploadFilePrefix + uploadPath);
            return JsonResponseBuilder.buildSuccessJsonResponse(map);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/richImage/upload", method = RequestMethod.POST)
    public Map richTextImageUpload(@RequestParam(name = "file") MultipartFile multipartFile, HttpSession session, HttpServletResponse response) {

        Map<String, Object> map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        if (user == null) {
            map.put("success", false);
            map.put("msg", ResponseCode.ERROR_USER_NOT_LOGIN.getMessage());
            return map;
        }
        JsonResponse<String> checkAdminResponse = userService.checkAdmin(user);
        if (!checkAdminResponse.isSuccess()) {
            map.put("success", false);
            map.put("msg", ResponseCode.ERROR_PERMISSION_DENIED.getMessage());
            return map;
        }
        String path = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("upload");
        try {
            String uploadPath = fileService.upload(multipartFile, path);
            if (StringUtils.isBlank(uploadPath)) {
                map.put("success", false);
                map.put("msg", ResponseCode.ERROR_SERVER_ERROR.getMessage());
                return map;
            }
            map.put("success", true);
            map.put("file_path", uploadFilePrefix + uploadPath);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return map;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            map.put("success", false);
            map.put("msg", ResponseCode.ERROR_PERMISSION_DENIED.getMessage());
            return map;
        }
    }

}
