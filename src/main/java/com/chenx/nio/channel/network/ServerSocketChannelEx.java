package com.chenx.nio.channel.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 *  通过这个简单的示例，可以看到在给ServerSocketChannel设置为非阻塞式的之后，
 *  accept方法不会阻塞，如果没有连接直接返回null，这就是所谓的非阻塞式。
 */
public class ServerSocketChannelEx {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting server...");
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);   // 设置为非阻塞模式
        String msg = "Local address: " + serverSocketChannel.socket().getLocalSocketAddress();
        msg += "hello, this is server.";
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        while (true) {
            System.out.print(".");
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 非null，即有连接进来，就执行操作，没连接进来就等会再检查
            if (socketChannel != null) {
                System.out.println();
                System.out.println("Received connection from " + socketChannel.socket().getRemoteSocketAddress());
                buffer.rewind();
                socketChannel.write(buffer);
                socketChannel.close();
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    // pass
                }
            }
        }
    }
}
