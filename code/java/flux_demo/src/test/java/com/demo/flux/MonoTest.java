package com.demo.flux;


import reactor.core.publisher.Mono;

public class MonoTest {
    public static void main(String[] args) {
        Mono.just("test").subscribe(System.out::println);
        // context
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(s -> Mono.deferContextual(ctx ->
                        Mono.just(s + " " + ctx.get(key))))
                .contextWrite(ctx -> ctx.put(key, "World 1"))
                .contextWrite(ctx -> ctx.put(key, "World 2"));
        r.subscribe(System.out::println); // Hello World 1
        //
    }
}
