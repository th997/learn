package com.demo.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Application {


    private final String topic = "test";
    private final String group = "testGroup";

    private static Set<String> threads = new HashSet<>();

    // 集群总消费线程数小于分区数
    @KafkaListener(id = group, topics = topic, concurrency = "10")
    public void listen(String input) {
        threads.add(Thread.currentThread().getName());
        System.out.println("consumer:" + input + threads);
        //throw new RuntimeException();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}