package com.github.th997.userserver.service;

import com.github.th997.userserver.service.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
public class UserServiceImpl implements UserService {


//    @ApolloConfig
//    private Config config;

    @Value("${test.name:init}")
    private String name;

    @Value("${test.name1:init}")
    private String name1;

    @Autowired
    private UserBean user1;

    @Autowired
    private UserBean user2;
    @Autowired
    private UserBean userx1;

    @Autowired
    private List<UserBean> userList;

    @Autowired
    private List<UserBean> userBeanList;

    @Autowired
    private Environment env;

    Logger log = LoggerFactory.getLogger(getClass());

//    @ApolloConfigChangeListener
//    private void onChangetest(ConfigChangeEvent changeEvent) {
//    }

    @Override
    public UserBean getUserTimeout(String id) {
        UserBean userBean = new UserBean();
        userBean.setId(id);
        userBean.setName(name);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userBean;
    }

    @Override
    public UserBean getUser(String id) {
        // System.out.println(env.getProperty("test.name"));
//        if (env instanceof StandardServletEnvironment) {
//            StandardServletEnvironment envs = (StandardServletEnvironment) env;
//            for (Map.Entry<String, Object> e : envs.getSystemEnvironment().entrySet()) {
//                System.out.println(e.getKey() + "=" + e.getValue());
//            }
//            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//            for (Map.Entry<String, Object> e : envs.getSystemProperties().entrySet()) {
//                System.out.println(e.getKey() + "=" + e.getValue());
//            }
//        }
        UserBean userBean = new UserBean();
        userBean.setId(id);
        userBean.setName(name);
        // log.info(userBean.toString());
        try {
            log.info("thread:" + Thread.currentThread().getName());
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userBean;
    }

    @Override
    public Callable<UserBean> getUser1(String id) {
        return () -> getUser(id);
    }
}
