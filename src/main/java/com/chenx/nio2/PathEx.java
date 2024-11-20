package com.chenx.nio2;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathEx {
    public static void main(String[] args) {
//        fileSystem();
        paths();
    }

    private static void paths() {
        // 根据
        Path path = Paths.get("a", "b", "C");
        System.out.println("Is absolute: " + path.isAbsolute());
        System.out.println("Absolute path: " + path.toAbsolutePath());
        System.out.println("File name: " + path.getFileName());
        System.out.println("File system: " + path.getFileSystem());
    }

    private static void fileSystem() {
        FileSystem fileSystem = FileSystems.getDefault();
//        System.out.println(fileSystem); // sun.nio.fs.WindowsFileSystem@182decdb

        Path path = fileSystem.getPath("a", "b", "c");
        System.out.println(path);
        System.out.println("File name：" + path.getFileName());
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println(path.getName(i));
        }
        System.out.println("Parent: " + path.getParent());
        System.out.println("Root: " + path.getRoot());
        System.out.println("SubPath: [0, 2): " + path.subpath(0, 2));
        System.out.println("Absolute path: " + path.toAbsolutePath());

        System.out.println(fileSystem.getRootDirectories());
    }

}
