package com.demo.spring_boot.web;

import com.demo.spring_boot.entity.Order;
import com.demo.spring_boot.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello(User user, Order order) {
        return "hello world:" + user + ",order=" + order;
    }
}

