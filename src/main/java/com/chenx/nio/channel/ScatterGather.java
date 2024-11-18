package com.chenx.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

import static com.chenx.nio.channel.PathConstants.BASE_PATH;


/**
 *  Scatter / Gather允许开发者将Channel的内容分散到多个Buffer组成的数组
 *  或将数组中的多个Buffer的内容收集到一个Channel
 */
public class ScatterGather {
    public static void main(String[] args) {
        scatterThenGather();
    }


    private static void scatterThenGather() {
        try (FileInputStream in = new FileInputStream(BASE_PATH + "scatter.txt");
             ScatteringByteChannel src = (ScatteringByteChannel) Channels.newChannel(in);
             FileOutputStream out = new FileOutputStream(BASE_PATH + "gather.txt")) {
            ByteBuffer buf1 = ByteBuffer.allocateDirect(5);
            ByteBuffer buf2 = ByteBuffer.allocateDirect(3);
            ByteBuffer[] buffers = {buf1, buf2};
            src.read(buffers);
            buf1.flip();
            while (buf1.hasRemaining())
                System.out.println((char) buf1.get());
            buf2.flip();
            while (buf2.hasRemaining())
                System.out.println((char) buf2.get());

            ///// gather
            // 将多个buffer集中到一个channel
            buf1.rewind();
            buf2.rewind();
            GatheringByteChannel dest;
            dest = ((GatheringByteChannel) Channels.newChannel(out));
            ByteBuffer[] bufferArr = {buf2, buf1};
            // 写入文件
            dest.write(bufferArr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
