package com.demo.java.buff;

import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer buff = ByteBuffer.allocate(1024);
        String str = "helloWorld";
        buff.put(str.getBytes());
        System.out.println(new String(buff.array()));
        System.out.println("position:" + buff.position() + "\t limit:"
                + buff.limit() + "\t capacity:" + buff.capacity());
        // 读取两个字节byte[] abytes = new byte[1];
        byte[] abytes = new byte[1];
        buff.get(abytes);
        System.out.println("get one byte to string:" + new String(abytes));
        // Reads the byte at this buffer's current position, and then increments
        // the position.
        buff.get();
        System.out.println("获取两个字节（两次get()方法调用）后");
        System.out.println("position:" + buff.position() + "\t limit:"
                + buff.limit());
        // Sets this buffer's mark at its position. like
        // ByteBuffer.this.mark=position
        buff.mark();
        System.out.println("mark()...");
        System.out.println("position:" + buff.position() + "\t limit:"
                + buff.limit());

        // 当读取到码流后，进行解码。首先对ByteBuffer进行flip操作，
        // 它的作用是将缓冲区当前的limit设置为position,position设置为0
        // flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
        buff.flip();
        System.out.println("flip()...");
        System.out.println("position:" + buff.position() + "\t limit:"
                + buff.limit() + "\t capacity:" + buff.capacity());

        byte[] tbyte = new byte[buff.limit()];
        buff.get(tbyte);
        System.out.println("get one byte to string:" + new String(tbyte));
        System.out.println("position:" + buff.position() + "\t limit:"
                + buff.limit());
        if (buff.hasRemaining()) {
            buff.compact();
        } else {
            buff.clear();
        }
    }
}
