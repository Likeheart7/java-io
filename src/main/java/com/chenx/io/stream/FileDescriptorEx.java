package com.chenx.io.stream;

import java.io.FileDescriptor;
import java.io.FileOutputStream;

public class FileDescriptorEx {
    public static void main(String[] args) throws Exception{
        // 每个进程都有默认的三个文件描述符
        // 0：stdin 1：stdout 2：stderr
        // jdk中将这三个标准fd封装在FileDescriptor中做静态常量
        FileOutputStream out = new FileOutputStream(FileDescriptor.out);
        out.write("hello".getBytes());
        out.close();    // 应当在finally / try-with-resource，此处省略
    }
}
