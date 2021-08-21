package com.demo.java.obj;

import java.util.HashSet;

public class EqualTest {
    public static void main(String[] args) {
        HashSet books = new HashSet();
        books.add(new A());
        books.add(new A());
        books.add(new B());
        books.add(new B());
        books.add(new C());
        books.add(new C());
        System.out.println(books);//2a2b1c
    }
}

class A {
    //类A的 equals 方法总是返回true,但没有重写其hashCode() 方法
    @Override
    public boolean equals(Object o) {
        return true;
    }
}

class B {
    //类B 的hashCode() 方法总是返回1，但没有重写其equals()方法
    @Override
    public int hashCode() {
        return 1;
    }
}

class C {
    public int hashCode() {
        return 2;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }
}