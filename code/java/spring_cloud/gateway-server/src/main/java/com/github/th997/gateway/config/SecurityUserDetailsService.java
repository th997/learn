package com.github.th997.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Configuration
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        log.info("findByUsername:{}", userName);
        // TODO
        if (!StringUtils.isEmpty(userName)) {
            UserDetails user = User.withUsername(userName)
                    .password(passwordEncoder.encode("123456"))
                    .authorities("user")
                    .build();
            return Mono.just(user);
        }
        return Mono.error(new UsernameNotFoundException("User Not Found"));

    }
}