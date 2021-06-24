package com.github.th997.userserver.config;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

@Configuration
public class BeanConfig {

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

    @Bean
    @Primary
    @ConditionalOnExpression("#{!T(org.springframework.util.StringUtils).isEmpty(environment.getProperty('test.name1'))}")
    public UserBean user3() {
        UserBean user = new UserBean();
        user.setId("user3");
        return user;
    }
}
