package com.github.th997.userserver.config;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BeanConfig implements InitializingBean {

    @Value("${test.name2:}")
    private String testName;

    @Bean
    public UserBean user1() {
        UserBean user = new UserBean();
        user.setId("user1");
        return user;
    }

    @Bean
    public UserBean user2() {
        UserBean user = new UserBean();
        user.setId("user2");
        return user;
    }

    @Autowired
    private GenericApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.registerBean("userx1", UserBean.class);
        applicationContext.registerBean("userx2", UserBean.class);
        applicationContext.registerBean("userx3", UserBean.class);
        applicationContext.getBean("userx1", UserBean.class).setName("userx1");
    }

    @Bean
    @Primary
    @ConditionalOnExpression("#{!T(org.springframework.util.StringUtils).isEmpty(environment.getProperty('test.name1'))}")
    public UserBean user3() {
        UserBean user = new UserBean();
        user.setId("user3");
        return user;
    }


}
