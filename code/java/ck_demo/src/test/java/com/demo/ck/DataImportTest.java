package com.demo.ck;

import com.demo.ck.entity.User;
import com.demo.ck.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataImportTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void testInsert() throws Exception {
        // 导入2000w数据
        BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream("/home/th/Downloads/data/2000w"), "gbk"));
        List<User> users = new ArrayList<>();
        int count = 0;
        String line;
        long start = System.currentTimeMillis();
        while ((line = br.readLine()) != null) {
            String[] ss = line.split(",");
            if (ss.length != 33) {
                //System.out.println(ss);
                continue;
            }
            User user = new User();
            for (int i = 1; i <= 33; i++) {
                BeanUtils.getPropertyDescriptor(User.class, "c" + i).getWriteMethod().invoke(user, ss[i - 1]);
            }
            users.add(user);
            count++;
            if (count < 0) {
                continue;
            }
            if (count % 1000 == 0) {
                //System.out.println(user);
                //userRepository.saveAll(users);
                users.clear();
                long cost = System.currentTimeMillis() - start;
                System.out.println("cost count=" + count + ",cost=" + cost);
                continue;
                // break;
            }
            if (users.size() > 0) {
                //userRepository.saveAll(users);
            }
        }
    }
}
