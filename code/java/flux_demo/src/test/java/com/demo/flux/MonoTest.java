package com.demo.flux;


import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class MonoTest {
    public static void main(String[] args) throws InterruptedException {
        Mono.just("test").subscribe(System.out::println);
        // context
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(s -> Mono.deferContextual(ctx ->
                        Mono.just(s + " " + ctx.get(key))))
                .contextWrite(ctx -> ctx.put(key, "World 1"))
                .contextWrite(ctx -> ctx.put(key, "World 2"));
        r.subscribe(System.out::println); // Hello World 1
        // Mono.deferContextual
        Mono.deferContextual(contextView -> {
            System.out.println(contextView.get("a").toString());
            return Mono.just(contextView.get("a"));
        }).contextWrite(context -> context.put("a", "b")).subscribe();
        // runable
        Scheduler scheduler = Schedulers.newBoundedElastic(3, 1000, "testThread", 5, false);
        for (int i = 0; i < 100; i++) {
            final int a = i;
            Mono.fromRunnable(() -> {
                System.out.println(Thread.currentThread().getName() + ":" + a);
            }).subscribeOn(scheduler).subscribe();
        }
    }
}
