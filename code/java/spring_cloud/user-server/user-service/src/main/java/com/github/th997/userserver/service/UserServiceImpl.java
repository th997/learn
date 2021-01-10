package com.github.th997.userserver.service;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.github.th997.userserver.service.bean.UserBean;
import com.vip.vjtools.vjkit.datamasking.DataMask;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceImpl implements UserService {

    public static void main(String[] args) {
        Object abc = new JSONObject();
        System.out.println(BeanUtils.isSimpleProperty(abc.getClass()));
    }

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
        return userBean;
    }
}
