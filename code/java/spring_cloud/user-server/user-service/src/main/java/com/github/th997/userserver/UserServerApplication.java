package com.github.th997.userserver;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class UserServerApplication {

    public static void main(String[] args) {
        System.setProperty("apollo.configService", "http://apollo-service-pro-apollo-configservice.com");
        System.setProperty("apollo.cluster", "cn");
        MDC.put("a", "b");
        SpringApplication.run(UserServerApplication.class, args);
    }
}
