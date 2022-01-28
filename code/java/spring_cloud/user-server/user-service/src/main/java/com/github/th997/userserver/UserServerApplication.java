package com.github.th997.userserver;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false)
//@EnableApolloConfig
@EnableCircuitBreaker
@EnableHystrix
public class UserServerApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        System.setProperty("apollo.configService", "http://apollo-service-pro-apollo-configservice:8080");
        System.setProperty("env", "pro");
        //System.setProperty("apollo.cluster", "cn");
        MDC.put("a", "b");
        SpringApplication.run(UserServerApplication.class, args);
    }
}