package com.github.th997.userserver.service;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.github.th997.userserver.service.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceImpl implements UserService {

//    @ApolloConfig
//    private Config config;

    @Value("${test.name:init}")
    private String name;

    @Value("${test.name1:init}")
    private String name1;

    Logger log = LoggerFactory.getLogger(getClass());

    @ApolloConfigChangeListener
    private void onChangetest(ConfigChangeEvent changeEvent) {
    }

    @Override
    public UserBean getUser(String id) {
        UserBean userBean = new UserBean();
        userBean.setId(id);
        userBean.setName(name);
        log.info(userBean.toString());
        return userBean;
    }
}
