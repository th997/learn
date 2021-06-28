package com.demo.ck;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.demo.ck.entity.User;
import com.demo.ck.repository.UserTkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTkRepositoryTest {

    @Autowired
    UserTkRepository userRepository;

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
        User user = userRepository.getOne();
        System.out.println(user);
        user = userRepository.getOne();
        System.out.println(user);
        user = userRepository.selectByPrimaryKey(2000000000);
        System.out.println(user);
        // ck
        Integer count = userRepository.countAll();
        System.out.println(count);
        testSwitchDs();
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
