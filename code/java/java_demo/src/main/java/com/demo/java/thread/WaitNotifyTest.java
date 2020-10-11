package com.demo.java.thread;

import java.util.Random;

// join 等待线程完成

public class WaitNotifyTest {
    private static Object lock = new Object();

    private static int i = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable run = new PrintThread();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
    }


    public static class PrintThread implements Runnable {
        public void run() {
            synchronized (this) {
                while (i <= 100) {
                    this.notify();
                    System.out.println(Thread.currentThread().getName() + "-" + i++);
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}


