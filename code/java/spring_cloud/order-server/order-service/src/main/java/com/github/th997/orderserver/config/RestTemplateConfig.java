package com.github.th997.orderserver.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${restTemplate.client.connectionRequestTimeout:9000}")
    private int connectionRequestTimeout;

    @Value("${restTemplate.client.connectTimeout:3000}")
    private int connectTimeout;

    @Value("${restTemplate.client.readTimeout:9000}")
    private int readTimeout;

    @Value("${restTemplate.client.maxConnPerRoute:150}")
    private int maxConnPerRoute;

    @Value("${restTemplate.client.maxConnTotal:500}")
    private int maxConnTotal;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }

    public HttpClient httpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setMaxConnPerRoute(maxConnPerRoute)
                .setMaxConnTotal(maxConnTotal);
        HttpClient client = httpClientBuilder.build();
        return client;
    }

}