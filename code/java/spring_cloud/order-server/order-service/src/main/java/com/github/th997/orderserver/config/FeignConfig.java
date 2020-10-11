package com.github.th997.orderserver.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.clientconfig.OkHttpFeignConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@Configuration
//@AutoConfigureAfter(OkHttpFeignConfiguration.class)
public class FeignConfig {
    // OkHttpFeignConfiguration

    private okhttp3.OkHttpClient okHttpClient;

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool httpClientConnectionPool(
            FeignHttpClientProperties httpClientProperties,
            OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        Integer maxTotalConnections = httpClientProperties.getMaxConnections();
        Long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory,
                                       ConnectionPool connectionPool,
                                       FeignHttpClientProperties httpClientProperties) {
        // h2
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.H2_PRIOR_KNOWLEDGE);

        Boolean followRedirects = httpClientProperties.isFollowRedirects();
        Integer connectTimeout = httpClientProperties.getConnectionTimeout();
        this.okHttpClient = httpClientFactory
                .createBuilder(httpClientProperties.isDisableSslValidation())
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects).connectionPool(connectionPool).protocols(protocols).build();
        return this.okHttpClient;
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }

}