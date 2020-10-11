package com.demo.java.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(100);
        cdl.countDown();
        System.out.println(cdl.getCount());
    }
}
