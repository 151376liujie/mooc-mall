package com.mall.controller;

import com.mall.vo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: jonny
 * Time: 2017-05-11 00:15.
 */
@RestController
public class HelloController {

    @RequestMapping(value = "hi")
    public UserInfo sayHello(){
        return new UserInfo(1,"jonny");
    }

}
