package com.demo.java.aqs;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        System.out.println(lock.tryLock());//true
        System.out.println(lock.tryLock());// true
    }
}
