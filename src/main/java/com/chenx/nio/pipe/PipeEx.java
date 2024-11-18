package com.chenx.nio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * java.nio包提供的Pipe，核心是sink（用于写入）和source（用于读出）两个方法。
 * 功能类似PipeInputStream和PipeOutputStream，可以在多个线程之间通信。
 */
public class PipeEx {
    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();
        Runnable sender = () -> {
            WritableByteChannel senderChannel = pipe.sink();
            ByteBuffer buffer = ByteBuffer.wrap("from sender".getBytes());
            System.out.println(buffer);
            try {
                while (senderChannel.write(buffer) > 0) ;
            } catch (IOException e) {
                System.err.println(e.getCause().toString());
            }
            try {
                senderChannel.close();
            } catch (IOException e) {
                System.err.println(e.getCause().toString());
            }
        };
        Runnable acceptor = () -> {
            ReadableByteChannel acceptorChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            try {

                while (acceptorChannel.read(buffer) > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.print((char) buffer.get());
                    }
                    buffer.clear();
                }
                acceptorChannel.close();
            } catch (IOException e) {
                System.err.println(e.getCause().toString());
            }
        };

        new Thread(sender).start();
        new Thread(acceptor).start();
    }
}
