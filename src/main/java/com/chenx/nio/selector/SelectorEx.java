package com.chenx.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Selector是Java提供的用于针对非阻塞式Channel实现多路复用的API。
 * 本质上Selector提供的功能是基于底层操作系统的.多路复用的本质，是操作系统内核支持同时管理多个FD，当其中某些FD有数据可以操作时，
 * 内核主动将其响应给应用程序，一般一次会返回多个。从而避免了阻塞和轮询。
 */
public class SelectorEx {
    private static final int DEFAULT_PORT = 9999;
    private static final ByteBuffer buf = ByteBuffer.allocateDirect(8);

    public static void main(String[] args) throws IOException {
        System.out.println("Server starting... listening on port " + DEFAULT_PORT);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(DEFAULT_PORT));
        ssc.configureBlocking(false);

        System.out.println("Server starting... listening on port " + 8888);
        ServerSocketChannel ssc2 = ServerSocketChannel.open();
        ssc2.bind(new InetSocketAddress(8888));
        ssc2.configureBlocking(false);

        // 一个Selector监听两个端口上的Accept事件
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT); // 注册对Accept事件感兴趣
        ssc2.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int n = selector.select(); // 一般会阻塞到有事件发生，但是也可能返回0
            // 处理返回0的情况
            if (n == 0) {
                System.out.println("select() return 0.");
                continue;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 因为只注册了Accept事件，所以只处理这个就可以
                if (key.isAcceptable()) {
                    ServerSocketChannel innerSsc = (ServerSocketChannel) key.channel();
                    int port = innerSsc.socket().getLocalPort();
                    SocketChannel sc = innerSsc.accept();
                    // 如果没有连接就绪时会返回null
                    if (sc == null) {
                        continue;
                    }
                    System.out.println(port + " receive connection.");
                    buf.clear();
                    buf.putLong(System.currentTimeMillis());
                    buf.flip();
                    System.out.println(port + " writing current time.");
                    while (buf.hasRemaining()) sc.write(buf);
                    sc.close(); // 处理完毕关闭SocketChannel
                }
                iter.remove();  // 每次处理完之后需要手动将key移除
            }
        }
    }
}
