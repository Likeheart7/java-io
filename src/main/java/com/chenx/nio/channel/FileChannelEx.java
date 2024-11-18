package com.chenx.nio.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.chenx.nio.channel.PathConstants.BASE_PATH;

public class FileChannelEx {
    public static void main(String[] args) throws IOException {
//        position();
        transfer();
    }

    /**
     * 通过Channel的transferTo/transferFrom实现快速的文件拷贝
     */
    private static void transfer() throws IOException{
        File file = new File(BASE_PATH + "/from.txt");
        if(!file.exists()) {
            if (file.createNewFile()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.writeBytes("goodbye yellow break road");
                raf.close();
            }
        }
        FileInputStream in = new FileInputStream(BASE_PATH + "from.txt");
        FileOutputStream out = new FileOutputStream(BASE_PATH + "to.txt");
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
    }

    /**
     * FileChannel可以通过position方法获取/设置position的位置
     * 这里的这个position，实际上就是底层操作系统维护的Opening File Table中的cur_position
     * 由此可知，position设置position的位置的实现也一定是C中的lseek方法或者类似的方法。
     * 永远要记得，底层只维护了一个cur_position，无论读写都会移动该指针。所以需要手动回拨等操作
     */
    private static void position() throws IOException {
        FileInputStream in = new FileInputStream(BASE_PATH + "scatter.txt");
        FileChannel channel = in.getChannel();
        // size方法返回文件的大小，单位字节
        System.out.println("Size of FileChannel: " + channel.size());
        channel.position(5);
        ByteBuffer buffer = ByteBuffer.allocate(20);
        channel.read(buffer);
        System.out.println(buffer);
        buffer.flip();
        while (buffer.hasRemaining())
            System.out.println((char) buffer.get());
        in.close();
    }
}
