package com.chenx.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

/**
 * 一个异步文件通过到示例和一个异步服务端通道示例。
 * 可以看到AIO的处理逻辑基于配置回调方法，但是处理起来相对复杂得多。
 */
public class AsynchronousChannelEx {
    public static void main(String[] args) throws Exception {
//        afc();
        assc();
    }

    /**
     * {@link java.nio.channels.AsynchronousServerSocketChannel} 异步服务端Channel
     */
    private static void assc() throws IOException {
        final int port = 9090;
        final String host = "localhost";

        AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open();
        assc.bind(new InetSocketAddress(host, port));
        System.out.println("Server listening at " + assc.getLocalAddress());
        Attachment attachment = new Attachment();
        attachment.channelServer = assc;
        assc.accept(attachment, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
            @Override
            public void completed(AsynchronousSocketChannel channelClient,
                                  Attachment att) {
                try {
                    SocketAddress clientAddr = channelClient.getRemoteAddress();
                    System.out.printf("Accepted connection from %s%n", clientAddr);
                    att.channelServer.accept(att, this);
                    Attachment newAtt = new Attachment();
                    newAtt.channelServer = att.channelServer;
                    newAtt.channelClient = channelClient;
                    newAtt.isReadMode = true;
                    newAtt.buffer = ByteBuffer.allocate(2048);
                    newAtt.clientAddr = clientAddr;
                    ReadWriteHandler rwh = new ReadWriteHandler();
                    channelClient.read(newAtt.buffer, newAtt, rwh);
                } catch (
                    IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t, Attachment att) {
                System.out.println("Failed to accept connection");
            }
        });

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Server terminated.");
        }
        assc.close();
    }

    /**
     * {@link AsynchronousFileChannel} 简单示例，方法返回的是个future，可以通过isDone判断是否完成
     *
     * @throws Exception
     */
    private static void afc() throws Exception {
        Path path = Paths.get(".", "pom.xml");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path);
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        Future<Integer> future = fileChannel.read(buf, 0);
        while (!future.isDone()) {
            System.out.println("waiting for complete");
        }
        System.out.println("Finish...");
        System.out.println("Byte read = " + future.get());
        buf.flip();
        while (buf.hasRemaining())
            System.out.print(((char) buf.get()));
        fileChannel.close();
    }

    static class Attachment {
        public AsynchronousServerSocketChannel channelServer;
        public AsynchronousSocketChannel channelClient;
        public boolean isReadMode;
        public ByteBuffer buffer;
        public SocketAddress clientAddr;
    }

    static class ReadWriteHandler
        implements CompletionHandler<Integer, Attachment> {
        private final static Charset CSUTF8 = Charset.forName("UTF-8");

        @Override
        public void completed(Integer result, Attachment att) {
            if (result == -1) {
                try {
                    att.channelClient.close();
                    System.out.printf("Stopped listening to client %s%n",
                        att.clientAddr);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return;
            }
            if (att.isReadMode) {
                att.buffer.flip();
                int limit = att.buffer.limit();
                byte bytes[] = new byte[limit];
                att.buffer.get(bytes, 0, limit);
                System.out.printf("Client at %s sends message: %s%n",
                    att.clientAddr,
                    new String(bytes, CSUTF8));
                att.isReadMode = false;
                att.buffer.rewind();
                att.channelClient.write(att.buffer, att, this);
            } else {
                att.isReadMode = true;
                att.buffer.clear();
                att.channelClient.read(att.buffer, att, this);
            }
        }

        @Override
        public void failed(Throwable t, Attachment att) {
            System.out.println("Connection with client broken");
        }
    }
}