package com.chenx.io.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileEx {
    public static void main(String[] args) {
        try {
//            intro();
//            basic();
            appendWrite();
        } catch (IOException e) {
            // pass
            e.printStackTrace();
        }
    }

    /**
     * 通过skipBytes/seek和write实现append写
     * skipBytes：相对于当前位置移动n位，相对位置操作，负值不会跳
     * seek：移动到第n位，绝对位置操作
     */
    private static void appendWrite() throws IOException {
        RandomAccessFile file = new RandomAccessFile("./demo", "rw");
//        file.seek(file.length()); // 绝对移动操作
        file.skipBytes((int)file.length()); // 相对移动操作
        file.write("hello".getBytes());
        file.close();
    }

    private static void basic() throws IOException {
        RandomAccessFile file = new RandomAccessFile("./demo", "rw");
//        file.setLength(1024); // 比原长度短会截断，长会扩展，扩展的内容全是二进制的00000000
        byte[] bytes = "this content".getBytes();
        file.write(bytes); // 写入内容
        long filePointer = file.getFilePointer();   // 就是os中的open file table该文件的cur_position，是一个读写共享的指针
        System.out.println(filePointer);    // 12
        System.out.println(file.read());    // 因为write操作移动了cur_position，所以读到的是-1，在Java中表示EOF
        file.getFD().sync();    // 使用该File对象对应的FD，强制要求os刷新缓冲区到磁盘，rwd模式也就是这个效果
        file.close();
    }

    private static void intro() throws IOException {
        // 第2个参数mode：
        // r：只读 rw：读写 rwd：读写且内容更新必须同步写入磁盘 rws：读写且内容和元信息更新必须同步写入磁盘
        // 当使用r模式访问不存在的文件或使用rw访问read-only文件时，会抛出FileNotFoundException
        RandomAccessFile file = new
            RandomAccessFile("./pom.xml", "r");
        // close会释放掉底层文件描述符表中的FD，让os的Open file table中的引用计数减1，也可能结束open file table中关于此文件的维护(refCount == 0)
        // 这些就是我们谈论file.close时涉及的资源释放，当然也包括jvm中关于此文件的相关资源的释放
        file.close(); // 一般放在finally / try-with-resource
    }
}
