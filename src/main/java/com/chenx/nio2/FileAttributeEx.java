package com.chenx.nio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 通过FileAttribute访问文件属性
 */
public class FileAttributeEx {
    public static void main(String[] args) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(Paths.get(".", "src"), BasicFileAttributes.class);
        System.out.println("Creation time: " + attributes.creationTime());
        System.out.println("File key: " + attributes.fileKey());
        System.out.println("Is Directory: " + attributes.isDirectory());
        System.out.println("Is regular file: " + attributes.isRegularFile());
        System.out.println("Is symbolic link: " + attributes.isSymbolicLink());
        System.out.println("Last access time: " + attributes.lastAccessTime());
        System.out.println("Last modified time: " + attributes.lastModifiedTime());
        System.out.println("Size: " + attributes.size());
    }
}
