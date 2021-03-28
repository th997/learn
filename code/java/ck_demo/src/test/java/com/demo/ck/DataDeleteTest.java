package com.demo.ck;

import com.demo.ck.entity.User;
import com.demo.ck.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class DataDeleteTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void testDelete() throws Exception {
        for (int i = 0; i < 1000000; i++) {
            User user = new User();
            user.setId(new Random().nextInt(2000 * 10000));
            userRepository.delete(user);
            Thread.sleep(2);
        }
    }
}
