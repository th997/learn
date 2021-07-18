package com.demo.es;

import com.demo.es.repository.UserMRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMRepositoryTest {

    @Autowired
    UserMRepository userRepository;


    @Test
    void testGet() {
        Object obj = userRepository.count();
        System.out.println(obj);
    }

}
