package com.github.th997.ordererver.service;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.POST;

@RequestMapping("/order/")
public interface OrderService {

    @GetMapping("getUser/{id}")
    UserBean getUser(@PathVariable("id") String id);
}
