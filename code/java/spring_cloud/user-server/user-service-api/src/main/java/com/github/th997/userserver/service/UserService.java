package com.github.th997.userserver.service;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.Callable;

@RequestMapping("/user")
public interface UserService {

    @GetMapping("getUserTimeout/{id}")
    UserBean getUserTimeout(@PathVariable("id") String id);

    @GetMapping("getUser/{id}")
    UserBean getUser(@PathVariable("id") String id);

    @GetMapping("getUser1/{id}")
    Callable<UserBean> getUser1(@PathVariable("id") String id);
}
