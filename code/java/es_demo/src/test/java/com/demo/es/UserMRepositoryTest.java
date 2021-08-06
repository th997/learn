package com.demo.es;

import com.demo.es.entity.User;
import com.demo.es.repository.UserMRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import java.util.List;

@SpringBootTest
public class UserMRepositoryTest {

    @Autowired
    UserMRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void testGet() {
        Object obj = userRepository.count();
        System.out.println(obj);
    }

    @Test
    void testFind() {
        mongoTemplate.indexOps(User.class).ensureIndex(new Index().background().on("c1", Sort.Direction.DESC));
        mongoTemplate.indexOps(User.class).ensureIndex(new Index().background().on("c2", Sort.Direction.DESC));
        List<User> obj = userRepository.findByC1("test");
        System.out.println(obj);
        System.out.println(obj.size());
        obj.forEach(u -> {
            u.setC2("test");
            u.setC4(null);
        });
        userRepository.save(obj.get(0));
        userRepository.saveAll(obj);
    }

}
