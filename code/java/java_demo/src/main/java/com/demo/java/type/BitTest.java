package com.demo.java.type;

import org.apache.commons.lang3.StringUtils;

public class BitTest {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(123));
        System.out.println(Long.valueOf("b'1110'".replaceAll("b|'", ""), 2));
        System.out.println("2099-01-01 00:00:00".matches("\\d{4}-[\\s\\S]*"));

    }
}
