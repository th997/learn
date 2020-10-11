package com.github.th997.orderserver.service;

import com.github.th997.ordererver.service.OrderService;
import com.github.th997.userserver.service.UserService;
import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Override
    public UserBean getUser(String id) {
        UserBean userBean = userService.getUser(id);
        return userBean;
    }
}
