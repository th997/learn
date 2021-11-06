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

    @Override
    public UserBean getUserTimeout(String id) {
        long start = System.currentTimeMillis();
        try {
            UserBean userBean = userService.getUserTimeout(id);
            return userBean;
        } catch (Exception e) {
            e.printStackTrace();
            UserBean userBean = new UserBean();
            userBean.setName("timeout=" + (System.currentTimeMillis() - start));
            return userBean;
        }
    }
}
