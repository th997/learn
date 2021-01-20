package com.demo.ck;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.demo.ck.entity.User;
import com.demo.ck.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testGetCount() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Integer count = userRepository.countAll();
        }
        System.out.println("cost=" + (System.currentTimeMillis() - start));
    }

    @Test
    void testGet() {
        // mysql
        User user = userRepository.findById(1).orElse(null);
        System.out.println(user);
        user = userRepository.findById(2000000000).orElse(null);
        System.out.println(user);
        // ck
        Integer count = userRepository.countAll();
        System.out.println(count);
    }

    @Test
    void testSwitchDs() {
        try {
            DynamicDataSourceContextHolder.push("ck");
            User user = userRepository.getOne();
            System.out.println(user);
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }
}
