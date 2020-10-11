package com.github.th997.ordererver.service;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/order/")
public interface OrderService {

    @GetMapping("getUser")
    UserBean getUser(String id);
}
