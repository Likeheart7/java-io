package com.chenx.io.stream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * {@link java.io.PipedOutputStream} 和 {@link  java.io.PipedInputStream} 用于实现线程间的通信
 */
public class PipeXxStreamEx {
    private static final String DATA = "Hello";

    public static void main(String[] args) throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        Runnable sender = () -> {
            try {
                for (int i = 0; i < DATA.length(); i++) {
                    pos.write(DATA.charAt(i));
                    System.out.println("[" + Thread.currentThread().getName() + "] write to output stream: " + DATA.charAt(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable accepter = () -> {
            try {
                int b;
                while ((b = pis.read()) != -1) {
                    System.out.println("[" + Thread.currentThread().getName() + "] accept from input stream: " + (char) b);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    pis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(sender).start();
        new Thread(accepter).start();
    }
}
