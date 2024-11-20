package com.chenx.nio2;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@link Files} 提供非常多的功能，这里仅作几个示例
 */
public class FilesEx {
    public static void main(String[] args) throws IOException {
//        lines();
//        readAllLines();
//        walk();
        createWriteThenRead();
    }

    private static void createWriteThenRead() throws IOException {
        Path path = Paths.get(".", "temp.txt");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.createFile(path);
        Files.write(path, "hello, i created you.".getBytes(), StandardOpenOption.WRITE);
        System.out.println(Files.readAttributes(path, "creationTime"));
        System.out.println(Files.readAllLines(path));
    }

    /**
     * readAllLines会将文件的内容全部读取到内存中，可能内存溢出
     */
    private static void readAllLines() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(".", "pom.xml"));
        lines.forEach(System.out::println);
    }

    /**
     * walk()可以遍历文件树
     */
    private static void walk() throws IOException {
        try(Stream<Path> pathStream = Files.walk(Paths.get(".", "src"))){
            pathStream.forEach(System.out::println);
       }
    }

    /**
     * lines方法返回的是一个Stream, 可以用来处理超大文件
     * @throws IOException
     */
    private static void lines() throws IOException {
        try(Stream<String> lines = Files.lines(Paths.get(".", "pom.xml"))) {
            lines.forEach(System.out::println);
        }
    }
}
