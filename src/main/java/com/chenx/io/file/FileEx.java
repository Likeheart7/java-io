package com.chenx.io.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;

public class FileEx {
    public static void main(String[] args) throws IOException {
        /* 注意，在maven项目中，路径起始是项目路径 */
        File file = new File("./pom.xml");
//        basicMethod(file);
//        canonicalPath(file);
//        getRootPath();
//        getSpace();
//        listFile();
//        checkPermission(file);
        compareMethod();
        getCurrentDir();
    }

    /**
     * 获取当前工作目录的几种方式
     */
    private static void getCurrentDir() {
        System.out.println("Current directory is: " + FileSystems.getDefault().getPath("").toAbsolutePath());
        System.out.println("Current directory is: " + Paths.get("").toAbsolutePath());
        System.out.println("Current directory is: " + new File("").getAbsolutePath());
    }

    /**
     * File对象之间的compareTo、equals和hashcode都是基于路径的，即使是同一个文件，使用不同的路径创建
     * 最终的equals、compareTo、hashcode的结果也会是0/false
     */
    private static void compareMethod() {
        System.out.println("========== CompareTo & equals & hashcode");
        File file1 = new File("./README.md");
        File file2 = new File(file1.getAbsolutePath());
        File file3 = new File(file1.getAbsolutePath());
        System.out.println("file1 equals file2: " + file2.equals(file1));
        System.out.println("file1 compareTo file2: " + file1.compareTo(file2));
        System.out.printf("file2's hashcode: %d, and file1's hashcode: %d\n", file2.hashCode(), file1.hashCode());

        System.out.println("file3 equals file2: " + file2.equals(file3));
        System.out.println("file3 compareTo file2: " + file3.compareTo(file2));
        System.out.printf("file2's hashcode: %d, and file3's hashcode: %d\n", file2.hashCode(), file3.hashCode());
    }

    /**
     * 通过File对象查看文件的read、write、execute权限，也可以通过对应的方法设置对应的该对象的权限
     */
    private static void checkPermission(File file) {
        System.out.println("========== Permission ==========");
        System.out.println("Can read: " + file.canRead());
        System.out.println("Can write: " + file.canWrite());
        System.out.println("Can execute: " + file.canExecute());
        boolean result = file.setReadOnly();
        System.out.println("Set readonly " + (result ? "success" : "failed"));
        System.out.println("Now can read: " + file.canRead());
        System.out.println("Now can write: " + file.canWrite());
        System.out.println("Now can execute: " + file.canExecute());
    }

    private static void listFile() {
        File file = new File("C:\\windows");
        System.out.println(file.exists());
        String[] fileNames = file.list((dir, name) -> name.endsWith(".exe"));
        for (String name : fileNames) {
            System.out.println(name);
        }
    }

    private static void getSpace() {
        System.out.println("========== Space ==========");
        File file = new File("D:");
        System.out.println(file.exists());
        System.out.println("Total space: " + file.getTotalSpace() / 1024 / 1024 + " MB");
        // 可用的空间，getUsableSpace会检查写入权限和其他操作系统限制
        System.out.println("UsableSpace space: " + file.getUsableSpace() / 1024 / 1024 + " MB");
        // 未分配的空间
        System.out.println("FreeSpace space: " + file.getFreeSpace() / 1024 / 1024 + " MB");

    }

    private static void getRootPath() {
        System.out.println("========== Root path ==========");
        File[] roots = File.listRoots();
        for (File root : roots) {
            System.out.println(root.getName());
        }
    }

    /**
     * getAbsolutePath可能返回包含相对路径标识的路径，
     * 不会解析路径中的符号链接（symlinks）、.（当前目录）、..（父目录）等
     * 路径中有冗余部分（如重复的 . 或 ..），这些冗余部分将保留
     * getCanonicalPath返回规范的标准绝对路径
     * 不过getCanonicalPath可能需要访问文件系统以解析路径
     */
    private static void canonicalPath(File file) throws IOException {
        System.out.println("========== Canonical path & Absolute Path ===========");

        System.out.println("Absolute path: " + file.getAbsolutePath());
        System.out.println("Canonical path: " + file.getCanonicalPath());
    }

    /**
     * 一些简单方法
     */
    private static void basicMethod(File file) {
        System.out.println("========== Basic method ===========");
        if (!file.exists()) { // exist() 文件是否存在
            System.out.println("File not found.");
            return;
        }

        System.out.println(file.isAbsolute());
        System.out.println(file.canRead()); // 能否读取
        System.out.println("Name: " + file.getName());
        System.out.println("Parent: " + file.getParent());
        System.out.println("Path: " + file.getPath());
        System.out.println("Last modified: " + Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        System.out.println("Length: " + file.length() + " bytes");
    }
}
