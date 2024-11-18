package com.chenx.nio.buffer;

import java.nio.ByteOrder;

/**
 * 处理关于不同架构的大小端问题
 * 常见的arm、x64、x86、risc-v一般默认都是小端序，有些可以配置。
 */
public class ByteOrderEx {
    public static void main(String[] args) {
        // 获取本机大小端信息
        // LITTLE_ENDIAN
        System.out.println(ByteOrder.nativeOrder());
    }
}
