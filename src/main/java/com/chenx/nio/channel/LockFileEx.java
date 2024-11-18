package com.chenx.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeUnit;

import static com.chenx.nio.channel.PathConstants.BASE_PATH;

/**
 *  同时启动多个实例，可以看到后面启动的实例需要等待前面启动的实例任务完成释放锁，才能开始执行自己的操作
 *  这就是FileChannel#lock实现的功能
 *  即使是强制终止了前面的实例，操作系统本身也会正确处理文件的锁问题。
 */
public class LockFileEx {
    public static void main(String[] args) throws Exception {
        FileOutputStream out = new FileOutputStream(BASE_PATH + "lockfile.txt");
        FileChannel channel = out.getChannel();
        Runnable task1 = () -> {
            FileLock lock = null;
            try {
                System.out.println("try to get lock...");
                lock = channel.lock();
                System.out.println("operation processing...");
                TimeUnit.SECONDS.sleep(10);
                System.out.println("operation finished!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (lock != null)
                        lock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task1).start();
    }
}
