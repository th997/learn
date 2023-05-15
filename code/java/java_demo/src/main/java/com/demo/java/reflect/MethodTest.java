package com.demo.java.reflect;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MethodTest {
    public static class User {
        public String testMethod0() {
            System.out.println("testMethod0");
            return "test";
        }

        public String testMethod1(String param1) {
            System.out.println("testMethod1");
            return param1;
        }

        public String testMethod2(String param1, String param2) {
            System.out.println("testMethod2");
            return param2;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(methodName(User::testMethod0));
        System.out.println(methodName(User::testMethod1));
        System.out.println(methodName(User::testMethod2));
    }


    @FunctionalInterface
    public interface Function0<T, R> extends Function<T, R>, Serializable {
        R apply(T t);
    }

    @FunctionalInterface
    public interface Function1<T, U, R> extends BiFunction<T, U, R>, Serializable {
    }

    @FunctionalInterface
    public interface Function2<T, U, V, R> extends Serializable {
        R apply(T t, U u, V v);
    }

    public static <T> String methodName(Function0<T, ?> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            return serializedLambda.getImplMethodName();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, U, R> String methodName(Function1<T, U, R> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            return serializedLambda.getImplMethodName();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, U, V, R> String methodName(Function2<T, U, V, R> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            return serializedLambda.getImplMethodName();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
