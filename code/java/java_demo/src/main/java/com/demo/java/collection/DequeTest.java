package com.demo.java.collection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class DequeTest {
    public static void main(String[] args) {
        //   Deque dq = new LinkedBlockingDeque();
        //   Deque dq = new LinkedList();
        Deque dq = new ArrayDeque();
        long start = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            dq.add(i);
            dq.getLast();
            dq.getFirst();
        }
        long end = System.nanoTime();
        System.out.println("time=" + (end - start) / 1000_000);
    }
}
