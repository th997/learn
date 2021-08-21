package com.github.th997.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
@Slf4j
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("onAuthenticationSuccess");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        // TODO
        //HttpHeaders httpHeaders = response.getHeaders();
        //httpHeaders.add("token","xxx");
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap("auth success".getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

}