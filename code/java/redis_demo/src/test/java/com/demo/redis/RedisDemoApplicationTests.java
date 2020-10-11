package com.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ValueOperations valueOperations;


    @Test
    public void test() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        valueOperations.get("test0");
        long start = System.currentTimeMillis();
        AtomicInteger ac = new AtomicInteger();
        int countI = 100;
        int countJ = 10000;
        int total = countI * countJ;
        CountDownLatch cdl = new CountDownLatch(total);
        for (int i = 0; i < countI; i++) {// 并发
            final int loc = i;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < countJ; j++) {// 请求次数
                            valueOperations.set("test" + loc * j, "test" + loc * j, 1000, TimeUnit.SECONDS);
                            valueOperations.get("test" + loc * j);
                            // System.out.println(obj);
                            cdl.countDown();
                            int co = total - (int) cdl.getCount();
                            if (co % 100000 == 0) {
                                System.out.println("time:" + (System.currentTimeMillis() - start) + ",num=" + co);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // break;

        }

        cdl.await();

    }


}
