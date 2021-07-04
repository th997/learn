package com.demo.ck;

import com.demo.ck.entity.User;
import com.demo.ck.repository.UserTkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.demo.ck.repository")
@RestController
public class Application {

    @Autowired
    private UserTkRepository userTkRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/test")
    public User test() {
        User user = userTkRepository.getOne();
        return user;
    }

}