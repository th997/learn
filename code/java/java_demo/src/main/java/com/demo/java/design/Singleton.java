package com.demo.java.design;

public class Singleton {
    public static void main(String[] args) {
        System.out.println(Singleton.getInstance());
    }

    private static volatile Singleton instance;

    private Singleton() {
    }

    private static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
