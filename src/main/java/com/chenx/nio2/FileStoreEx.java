package com.chenx.nio2;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 通过FileStore访问空间信息
 */
public class FileStoreEx {
    public static void main(String[] args) throws IOException {
        FileSystems.getDefault().getFileStores().forEach(FileStoreEx::printSpace);

        FileStore fs = Files.getFileStore(Paths.get("."));
        System.out.println("Total space:" + fs.getTotalSpace());
        System.out.println("Unallocated space:" + fs.getUnallocatedSpace());
        System.out.println("Usable space:" + fs.getUsableSpace());
        System.out.println("Read only:" + fs.isReadOnly());
        System.out.println("Name:" + fs.name());
        System.out.println("Type:" + fs.type());
    }
    private static void printSpace(FileStore fs) {
        try {
            System.out.println("Total space of " + fs.name() + ":" + fs.getTotalSpace());
        } catch (IOException e) {
            // pass
        }
    }
}
