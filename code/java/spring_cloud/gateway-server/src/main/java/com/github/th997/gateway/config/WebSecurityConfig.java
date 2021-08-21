package com.github.th997.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

//@EnableWebFluxSecurity
public class WebSecurityConfig {

//    @Value("jwt_sign_key:ghqHaPI4a4sdw213UGI123OF39GUIae978aSdoUPF")
//    private String jwtSignKey;
//
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(jwtSignKey);
//        return converter;
//    }
//
//    @Bean
//    public JwtTokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


    private static final String[] excludedAuthPages = {
            "/oauth/token",
            "/login",
    };

    @Autowired
    private ServerAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private ServerAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.cors()
                .and().authorizeExchange().pathMatchers(excludedAuthPages).permitAll().anyExchange().authenticated()
                .and().formLogin()
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .authenticationFailureHandler(authenticationFailureHandler)
                .and().csrf().disable()
                .logout().disable();
        return http.build();
    }
}