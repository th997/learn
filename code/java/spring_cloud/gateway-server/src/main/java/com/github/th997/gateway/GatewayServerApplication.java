package com.github.th997.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class GatewayServerApplication {
    
    public static void main(String[] args) {
        System.setProperty("apollo.configService", "http://apollo-service-pro-apollo-configservice:8080");
        System.setProperty("env", "pro");
        SpringApplication.run(GatewayServerApplication.class, args);
    }
}
