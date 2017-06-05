package com.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Constants;
import com.mall.common.JsonResponse;
import com.mall.common.JsonResponseBuilder;
import com.mall.common.ResponseCode;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.dao.UserMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.CartService;
import com.mall.util.BigDecimalUtil;
import com.mall.vo.CartProductVO;
import com.mall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-28 10:27.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Value("${ftp.server.http.prefix}")
    private String imageHost;

    @Override
    public JsonResponse addProductToCart(Integer userId, Integer productId, Integer count) {
        if (userId == null || productId == null || count == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null || user.getRole() != Constants.Role.ROLE_CUSTOMER) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_EXISTS);
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            cart = addNewProduct2Cart(productId, userId, count);
            cartMapper.insert(cart);
        } else {
            cart.setQuantity(count + cart.getQuantity());
            cart.setUpdateTime(new Date());
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.listProduct(userId);
    }

    @Override
    public JsonResponse updateCart(Integer userId, Integer productId, Integer count) {
        if (userId == null || productId == null || count == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null || user.getRole() != Constants.Role.ROLE_CUSTOMER) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_EXISTS);
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_ENTITY_NOT_EXISTS);
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.listProduct(userId);
    }

    @Override
    public JsonResponse deleteProduct(Integer userId, String productIds) {
        if (userId == null || StringUtils.isBlank(productIds)) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null || user.getRole() != Constants.Role.ROLE_CUSTOMER) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_USER_NOT_EXISTS);
        }

        List<String> list = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(list)) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        cartMapper.deleteProductByUserIdAndProductIds(userId, list);
        return this.listProduct(userId);
    }


    @Override
    public JsonResponse listProduct(Integer userId) {

        if (userId == null) {
            return JsonResponseBuilder.buildErrorJsonResponseWithoutData(ResponseCode.ERROR_PARAMETER);
        }
        CartVO cartVO = this.getCartVO(userId);
        return JsonResponseBuilder.buildSuccessJsonResponse(cartVO);
    }

    @Override
    public JsonResponse selectOrUnselect(Integer userId, Integer productId, Integer check) {
        cartMapper.checkedOrUncheckedProduct(userId, productId, check);
        return this.listProduct(userId);
    }

    @Override
    public JsonResponse countProduct(Integer userId) {
        if (userId == null) {
            return JsonResponseBuilder.buildSuccessJsonResponse(0);
        }
        int count = cartMapper.countProductByUserId(userId);
        return JsonResponseBuilder.buildSuccessJsonResponse(count);
    }

    private CartVO getCartVO(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVO> cartProductVOS = Lists.newArrayList();
        BigDecimal totalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setUserId(userId);
                cartProductVO.setId(cartItem.getId());
                cartProductVO.setProductId(cartItem.getProductId());
                cartProductVO.setProductChecked(cartItem.getChecked());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductSubTitle(product.getSubtitle());
                    cartProductVO.setProductStock(product.getStock());
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        cartProductVO.setLimitQuantity(Constants.Cart.LIMIT_NUM_SUCCESS);
                        buyLimitCount = cartItem.getQuantity();
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVO.setLimitQuantity(Constants.Cart.LIMIT_NUM_FAILED);
                        Cart cart = new Cart();
                        cart.setId(cartItem.getId());
                        cart.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVO.setQuantity(buyLimitCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.multi(buyLimitCount, product.getPrice().doubleValue()));
                }
                if (cartItem.getChecked() == Constants.Cart.CHECKED) {
                    totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOS.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOS);
        cartVO.setCartTotalPrice(totalPrice);
        cartVO.setImageHost(imageHost);
        cartVO.setAllChecked(checkProductCheckStatus(userId));
        return cartVO;
    }


    private boolean checkProductCheckStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        int count = cartMapper.checkCartProductCheckStatusByUserId(userId);
        return count == 0;
    }

    private Cart addNewProduct2Cart(Integer productId, Integer userId, Integer count) {
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setUserId(userId);
        cart.setQuantity(count);
        cart.setChecked(Constants.Cart.CHECKED);
        return cart;
    }
}
