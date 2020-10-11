package com.demo.java.thread;

import java.util.Random;

// join 等待线程完成

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        OrderThread t1 = new OrderThread("t1");
        OrderThread t2 = new OrderThread("t2");
        t1.start();
        // t1.join(); 顺序执行
        t2.start();
        t1.join();
        t2.join();
    }


    private static class OrderThread extends Thread {
        private String name;

        public OrderThread(String name) {
            this.name = name;
        }

        public void run() {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("do something:" + name);
        }
    }
}


