package com.demo.java.threadlocal;

public class ThreadLocalTest {
    public static void main(String[] args) throws InterruptedException {
        final ThreadLocal<Integer[]> threadLocal = new ThreadLocal();
        System.out.println(Runtime.getRuntime().freeMemory());
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                threadLocal.set(new Integer[10000]);
            }).start();
        }
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Thread.sleep(1000);
            System.out.println(Runtime.getRuntime().maxMemory());
        }

    }
}
