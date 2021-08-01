package com.demo.es;

import com.demo.es.entity.User;
import com.demo.es.repository.UserMRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataMImportTest {
    @Autowired
    UserMRepository userMRepository;
    @Autowired
    MongoClient mongoClient;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void testInsert() throws Exception {
        System.out.println(mongoClient.listDatabaseNames().first());
        MongoCollection userTable = mongoClient.getDatabase("test").getCollection("user");
        // 导入2000w数据
        BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream("/mnt/l/data/2000w"), "gbk"));
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
            count++;
            User user = new User();
            for (int i = 1; i <= 33; i++) {
                BeanUtils.getPropertyDescriptor(User.class, "c" + i).getWriteMethod().invoke(user, ss[i - 1]);
            }
            users.add(user);
            if (users.size() >= 10000) {
                userMRepository.saveAll(users);
                //mongoTemplate.insert(users,User.class);
                users.clear();
                long cost = System.currentTimeMillis() - start;
                System.out.println("count=" + count + ",cost=" + cost);
                // break;
            }
        }
        if (users.size() > 0) {
            userMRepository.saveAll(users);
        }
    }
}
