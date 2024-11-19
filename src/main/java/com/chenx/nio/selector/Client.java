package com.chenx.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Client {
    static final ByteBuffer buf = ByteBuffer.allocateDirect(8);

    public static void main(String[] args) throws IOException {
        connect(9999);
        connect(8888);
    }

    private static void connect(int port) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(port));
        long time = 0;
        while (sc.read(buf) != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                time <<= 8;
                time |= buf.get() & 255;
            }
            buf.clear();
        }
        System.out.println("accept message from port " + port + ": " + new Date(time));
        sc.close();
    }
}
