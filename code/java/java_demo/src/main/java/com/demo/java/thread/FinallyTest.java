package com.demo.java.thread;

public class FinallyTest {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("addShutdownHook ok");
                })
        );
        try {
            System.out.println("do something start");
            Thread.sleep(60 * 1000);
            System.out.println("do something end");
        } finally {
            System.out.println("finally ok");
        }
    }
}
