package com.demo.flux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxTest {
    public static void main(String[] args) {
        //https://skyao.io/learning-reactor/docs/concept/operator.html
        // just
        Flux.just("test1", "test2").subscribe(System.out::println);
        // from
        List<String> iterable = Arrays.asList("test1", "test2");
        Flux.fromIterable(iterable).subscribe(System.out::println);
        Flux.fromStream(iterable.stream()).subscribe(System.out::println);
        Flux.fromArray(iterable.toArray()).subscribe(System.out::println);
        // range
        Flux.range(1, 3).subscribe(System.out::println);
        // subscribe
        Flux.range(-1, 3).map(i -> 3 / i)
                .onErrorReturn(0).onErrorContinue((e, a) -> System.out.println(String.format("%s,%s", e.getMessage(), a)))
                .subscribe(
                        System.out::println,
                        System.err::println,
                        () -> System.out.println("Completed!"));
        // map
        Flux.just("123").map(item -> Integer.parseInt(item)).doOnNext(item -> System.out.println(item));// do nothing
        Flux.just("123").map(item -> Integer.parseInt(item)).doOnNext(item -> System.out.println(item)).subscribe();
        // generate create sink
        Flux.generate(sink -> {
            sink.next("test1");
            sink.complete();
        }).subscribe(System.out::println);
        Flux.create(sink -> {
            sink.next("test1");
            sink.next("test2");
            sink.complete();
        }).subscribe(System.out::println);
        // Sinks.one().asMono().block(Duration.ofMillis(1000));
        // buffer
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        // filter
        Flux.range(1, 4).filter(i -> i % 2 == 0).subscribe(System.out::println);
        // reduce sum(1..100)
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        // zip
        Flux.just("a", "b").zipWith(Flux.just("c", "d")).subscribe(System.out::println);
        Flux.just("a", "b").zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2)).subscribe(System.out::println);
        // take  1..5
        Flux.range(1, 1000).take(5).subscribe(System.out::println);
        // merge 1234
        Flux.merge(Flux.range(1, 2), Flux.range(3, 2)).subscribe(System.out::print);
        //
    }
}
