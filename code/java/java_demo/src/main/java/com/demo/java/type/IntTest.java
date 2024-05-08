package com.demo.java.type;

public class IntTest {
    public static void main(String[] args) {
        int a1 = 1_000;
        System.out.println(a1 << 1);
        byte a = 127;
        byte b = 127;
       // b = a + b; // error : cannot convert from int to byte
        b += a; // ok
        System.out.println(b);
        System.out.println(3*0.1 );
    }
}
