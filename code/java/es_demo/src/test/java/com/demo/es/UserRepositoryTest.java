package com.demo.es;

import com.demo.es.entity.User;
import com.demo.es.repository.UserRepository;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.InternalSum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void test1() {
        System.out.println(userRepository.count());
        for (int i = 0; i < 100; i++) { // 最大只能查10000条
            Pageable page = PageRequest.of(0, 100);
            Page<User> pageList = userRepository.findAll(page);
            if (!pageList.hasContent()) {
                System.out.println("finish");
                break;
            }
            for (User user : pageList.getContent()) {
                System.out.println(user);
            }
        }
    }

    @Test
    public void test2() { // 无法查询
        Iterable<User> users = userRepository.findAll();
        AtomicInteger count = new AtomicInteger();
        users.forEach((user -> {
            count.addAndGet(1);
            System.out.println(user);
        }));
        System.out.println(count.get());
    }

    @Test
    public void test3() {
    }
}
