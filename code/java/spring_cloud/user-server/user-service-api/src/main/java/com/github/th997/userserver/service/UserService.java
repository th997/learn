package com.github.th997.userserver.service;

import com.github.th997.userserver.service.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserService {

    @GetMapping("getUser/{id}")
    UserBean getUser(@PathVariable("id") String id);
}
