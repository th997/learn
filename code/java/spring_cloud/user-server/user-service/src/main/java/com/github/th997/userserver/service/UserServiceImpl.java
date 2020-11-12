package com.github.th997.userserver.service;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceImpl implements UserService {

//    @ApolloConfig
//    private Config config;

    @Value("${test.name:init}")
    private String name;

    @Value("${test.name1:init}")
    private String name1;

    @Override
    public UserBean getUser(String id) {
        UserBean userBean = new UserBean();
        userBean.setId(id);
        userBean.setName(name);
        return userBean;
    }
}
