package com.demo.es;

import com.demo.es.entity.User;
import com.demo.es.repository.UserRepository;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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

    // https://github.com/a601942905git/boot-example/blob/master/boot-example-advance/boot-example-elasticsearch/src/test/java/com/boot/example/EsTest.java
    // https://blog.csdn.net/wenwen513/article/details/85163168
    // https://blog.csdn.net/lixiang19971019/article/details/105009148
    // https://elasticsearchjava-api.readthedocs.io/en/latest/aggregation.html

    @Test
    public void testCount() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.count("count_c7").field("c7"))
                .withSourceFilter(new FetchSourceFilterBuilder().build())
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query, User.class);
        for (String key : searchHits.getAggregations().asMap().keySet()) {
            ParsedValueCount value = searchHits.getAggregations().get(key);
            System.out.println(key + " " + value.getValue());
        }
    }

    @Test
    public void testCountDistinct() { // （有少量误差）
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.cardinality("count_distinct_c7").field("c7"))
                .withSourceFilter(new FetchSourceFilterBuilder().build())
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query, User.class);
        for (String key : searchHits.getAggregations().asMap().keySet()) {
            ParsedCardinality value = searchHits.getAggregations().get(key);
            System.out.println(key + " " + value.getValue());
        }
    }

    // select c7,count(*) co from xx  group by co order by co desc limit 15
    @Test
    public void testGroup() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.terms("group_c7").field("c7").size(15))
                .withSourceFilter(new FetchSourceFilterBuilder().build())
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query, User.class);
        for (String key : searchHits.getAggregations().asMap().keySet()) {
            ParsedStringTerms brands = searchHits.getAggregations().get(key);
            brands.getBuckets().forEach(bucket -> {
                System.out.println(bucket.getKeyAsString() + " " + bucket.getDocCount());
            });
        }
    }


    // https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#reference
    @Test
    public void testScroll() {
        int scrollTimeInMillis = 60000;
        IndexCoordinates index = IndexCoordinates.of("user");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(PageRequest.of(0, 10000))
                .build();
        SearchScrollHits<User> scroll = elasticsearchRestTemplate.searchScrollStart(scrollTimeInMillis, searchQuery, User.class, index);
        String scrollId = scroll.getScrollId();
        int count = 0;
        while (scroll.hasSearchHits()) {
//            scroll.getSearchHits().forEach(userSearchHit -> {
//                System.out.println(userSearchHit.getContent());
//            });
            count += scroll.getSearchHits().size();
            System.out.println(count);
            scrollId = scroll.getScrollId();
            scroll = elasticsearchRestTemplate.searchScrollContinue(scrollId, scrollTimeInMillis, User.class, index);
        }
        elasticsearchRestTemplate.searchScrollClear(Arrays.asList(scrollId));
    }
}
