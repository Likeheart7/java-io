package com.chenx.nio2;

import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class FileSystemProviderEx {
    public static void main(String[] args) {
        List<FileSystemProvider> providers = FileSystemProvider.installedProviders();
        /*
        sun.nio.fs.WindowsFileSystemProvider@64c64813
        com.sun.nio.zipfs.ZipFileSystemProvider@3ecf72fd
         */
        providers.forEach(System.out::println);
    }
}
