package com.github.th997.orderserver;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableApolloConfig
public class OrderServerApplication {

    public static void main(String[] args) {
        System.setProperty("apollo.configService", "http://apollo-service-pro-apollo-configservice:8080");
        System.setProperty("env", "pro");
        SpringApplication.run(OrderServerApplication.class, args);
    }

}
