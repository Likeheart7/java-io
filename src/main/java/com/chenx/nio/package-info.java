package com.chenx.nio;
/*
 * 本包主要包括java.nio提供的类的示例。
 * 注意，一般在finally中或try-with-resource处理close，示例可能简单处理了。
 * 什么是非阻塞式？
 *  传统BIO，如ServerSocket的accept方法，会一直阻塞直到有客户端连接进来，而在非阻塞情况下，accept如果有连接就返回对应的SocketChannel，
 *  否则返回null，所以我们需要while来判断。非阻塞的目的是让原本阻塞等待的时间变得有意义，如果只是单纯的while等待，那么不仅没意义，还可能浪费CPU时钟。
 *  所以需要Selector，可以简单地认为非阻塞式的最终目的是为了配合实现多路复用
 */