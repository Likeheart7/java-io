package com.chenx.io.stream;

import java.io.*;

/**
 * <pre>
 * 使用{@link DataInputStream} 和 {@link DataOutputStream} 可以更轻松地处理对不同类型数据的读取和写入
 * DataOutputStream在写出字符串时，格式是 长度+编码，所以如果不是用DataInputStream读取，可能会获取到额外的两个字节的长度信息，这可能会产生问题
 * </pre>
 */
public class DataXxStreamEx {
    public static void main(String[] args) {
        out();
        in();
    }

    private static void out() {
        try (FileOutputStream fos = new FileOutputStream("./demo");
             DataOutputStream dos = new DataOutputStream(fos)) {
            dos.writeInt(2015);
            dos.writeUTF("第二十六中学");
            dos.writeFloat(1.0F);
            dos.writeBoolean(false);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void in() {
        try (FileInputStream fis = new FileInputStream("./demo");
             DataInputStream dis = new DataInputStream(fis)) {
            System.out.println(dis.readInt());
            System.out.println(dis.readUTF());
            System.out.println(dis.readFloat());
            System.out.println(dis.readBoolean());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
