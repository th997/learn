package com.demo.java.date;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class Test1 {
    public static void main(String[] args) {
        System.out.println(Instant.now().getEpochSecond());
        System.out.println(Instant.now().toEpochMilli());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
