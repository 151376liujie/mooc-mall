package com.mall.dao;

import com.mall.pojo.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Author: jonny
 * Time: 2017-05-29 06:00.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
public class TestCartDao {

    @Autowired
    private CartMapper cartMapper;


    @Test
    public void testGetCartById(){
        Cart cart = cartMapper.selectByPrimaryKey(126);
        System.out.println(cart);
    }

    @Test
    public void testGetCartByUserId(){
        List<Cart> cart = cartMapper.selectByUserId(22);
        System.out.println(cart);
    }

    @Test
    public void testGetCartByUserIdAndProductId(){
        Cart cart = cartMapper.selectByUserIdAndProductId(22, 26);
        System.out.println(cart);
    }

}
