package com.chenx.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.chenx.nio.channel.PathConstants.BASE_PATH;

/**
 * 通过FileChannel的map方法将其映射到一个MappedByteBuffer上，对这个对象的修改将作用到文件上
 */
public class MappedEx {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(BASE_PATH + "gather.txt", "rw");
        FileChannel channel = file.getChannel();
        long size = channel.size();
        System.out.println("File size: " + size);
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        while (map.hasRemaining())
            System.out.print(((char) map.get()));
        System.out.println();
        // 更改索引为7的位置的值，对应文件也会被修改
        map.put(7, ((byte) 98));
        map.force(); // 刷出
        channel.close();
        file.close();
    }
}
