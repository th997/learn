package com.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
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

    @Autowired
    private RedissonClient redissonClient;

    // 自动续约  https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
    @Test
    public void test2() throws InterruptedException {
        String lockKey = "xxxaaaasd123";
        RLock lock1 = redissonClient.getLock(lockKey);
        if (lock1.tryLock(0, 20, TimeUnit.SECONDS)) {
            System.out.println("lock1 ok");
            Thread t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        RLock lock2 = redissonClient.getLock(lockKey);
                        if (lock2.tryLock()) {
                            System.out.println("lock2 ok");
//                            lock2.unlock();
//                            System.out.println("lock2 unlock");
                            break;
                        } else {
                            System.out.println("lock2 faild:" + lock2.remainTimeToLive());
                            try {
                                TimeUnit.SECONDS.sleep(4);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            t.start();
            TimeUnit.SECONDS.sleep(35);
            lock1.unlock();
            System.out.println("lock1 unlock");
            t.join();
        }
    }

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
                            valueOperations.set("test" + loc * j, "test" + loc * j, 100, TimeUnit.SECONDS);
                            valueOperations.get("test" + loc * j);
                            // System.out.println(obj);
                            cdl.countDown();
                            int co = total - (int) cdl.getCount();
                            if (co % 100000 == 0) {
                                System.out.println("time:" + (System.currentTimeMillis() - start) + ",num=" + co);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            // break;
            //redisTemplate.keys("a");
        }

        cdl.await();

    }


}
