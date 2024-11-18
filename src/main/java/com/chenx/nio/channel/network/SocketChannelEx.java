package com.chenx.nio.channel.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 传统BIO在connect方法处阻塞，直到连接成功。
 * 当设置为非阻塞式模式时，需要通过配置finishConnect()判断连接完成。
 * 并且必须通过while循环读入Buffer，否则可能读到空内容
 */
public class SocketChannelEx {
    public static void main(String[] args) throws IOException {
//        blocking();
        nonBlocking();
    }

    private static void nonBlocking() throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);    // 设置为非阻塞式
        sc.connect(new InetSocketAddress(9999));
        // 使用非阻塞式SocketChannel必须不断调用此方法，直到连接完成
        while (!sc.finishConnect()) {
            System.out.println("waiting for finish connection...!");
        }
        ByteBuffer buf = ByteBuffer.allocate(100);
        // 必须通过while读取，否则可能读到空的
        while (sc.read(buf) >= 0) {
            buf.flip();
            while (buf.hasRemaining())
                System.out.print((char) buf.get());
            buf.clear();
        }
        sc.close();
    }

    private static void blocking() throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(9999));
        ByteBuffer buffer = ByteBuffer.allocate(100);
        sc.read(buffer);
        buffer.flip();
        while (buffer.hasRemaining())
            System.out.print((char) buffer.get());
        sc.close();
    }
}
