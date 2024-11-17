package com.chenx.io.stream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class InputStreamEx {
    public static void main(String[] args) throws IOException {
        // 最基础也是最慢的拷贝文件最慢的方式
        try (FileInputStream in = new FileInputStream("./1.txt");
             FileOutputStream out = new FileOutputStream("./2.txt")) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        }
    }
}
