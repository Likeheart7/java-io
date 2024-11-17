package com.chenx.io.readerwriter;

import java.io.FileReader;
import java.io.FileWriter;

public class FileReaderWriterEx {
    public static void main(String[] args) throws  Exception{
        String message = "dont look back in anger";
        try (FileWriter fw = new FileWriter("./demo")) {
            fw.write(message, 0, message.length());
        }
        char[] chars = new char[message.length()];
        // 如果在write操作前创建fileReader，会读出乱码
        try (FileReader fr = new FileReader("./demo")) {
            fr.read(chars, 0, message.length());
            System.out.println(chars);
        }
    }
}
