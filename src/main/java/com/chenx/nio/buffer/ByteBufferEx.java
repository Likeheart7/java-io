package com.chenx.nio.buffer;


import java.nio.ByteBuffer;

public class ByteBufferEx {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        System.out.println("Capacity: " + buffer.capacity());
        System.out.println("Limit: " + buffer.limit());
        System.out.println("Position: " + buffer.position());
        System.out.println("Remaining: " + buffer.remaining());
        System.out.println("===== Set Buffer limit to 5");
        buffer.limit(5);
        System.out.println("Limit: " + buffer.limit());
        System.out.println("Position: " + buffer.position());
        System.out.println("Remaining: " + buffer.remaining());
        System.out.println("===== Changing buffer position to 3");
        buffer.position(3);
        System.out.println("Limit: " + buffer.limit());
        System.out.println("Position: " + buffer.position());
        System.out.println("Remaining: " + buffer.remaining());
        System.out.println(buffer);
    }
}
