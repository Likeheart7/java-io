package com.chenx.io.stream;

import java.io.PrintStream;

/**
 * 首先，因为PrintStream不支持自定义编码格式，所以一般用PrintWriter
 * 我们常用的System.out，这个out静态实例就是PrintStream
 */
public class PrintStreamEx {
    public static void main(String[] args) {
        PrintStream out = System.out;
        System.out.println(out.getClass()); // class java.io.PrintStream
    }
}
