package com.chenx.nio.buffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteBufferInDepth {
    public static void main(String[] args) {
       allocateWrapDup();
        getAndPut();
        flip();
        mark();
        compact();
    }

    /**
     * compact()：将Buffer中position -> limit之间的数据拷贝到Buffer开头，用于处理未读取完Buffer中的数据的情况
     */
    private static void compact() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        buffer.put((byte) 5);
        buffer.flip();
        // 读取三个
        buffer.get();
        buffer.get();
        buffer.get();
        //此时 position = 3, limit=5
        buffer.compact(); // 拷贝3 - 5 之间的数据到开头，并将position设置为3，limit设置为capacity
        // 实现的结果就是下次写入在上次剩余数据后面接着写入，不会覆盖原来的数据
        buffer.put(((byte) 22));
        buffer.put(((byte) 23));
        /*
        [4, 5, 22, 23, 5, 0, 0, 0, 0, 0]
        注意最后那个5是在将被覆盖的数据，所以这种情况要保证写入会覆盖掉上次拷贝剩下的数据，否则可能产生误解
         */
        System.out.println(Arrays.toString(buffer.array()));
    }

    /**
     * 通过mark + reset，重置position到之前mark过的位置
     */
    private static void mark() {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.put((byte) 10);
        buffer.put((byte) 20);
        buffer.put((byte) 30);
        buffer.put((byte) 40);
        buffer.limit(4);
        // mark + reset允许将position回到mark的地点
        buffer.position(1).mark().position(3);
        /*
        40 20 30 40
         */
        while (buffer.hasRemaining())
            System.out.print(buffer.get() + " ");
        buffer.reset();
        while (buffer.hasRemaining())
            System.out.print(buffer.get() + " ");
    }

    /**
     * Buffer类似操作系统对文件的访问，没有读写独立指针，所以在写入数据之后，一般需要手动调用flip方法将position和limit重置
     * 实际上flip() == buffer.limit(buffer.position()).position(0).mark(-1)
     */
    private static void flip() {
        /* flip == limit(buf.position()).position(0); 用于在填充完成之后，重置回去用于读取*/
        byte[] bytes = new byte[100];
        Arrays.fill(bytes, ((byte) 29));
        ByteBuffer buf = ByteBuffer.allocate(50);
        buf.put(bytes, 10, 50);
        System.out.println(buf);
        System.out.println("Remain: " + buf.remaining());
        // 在调用relative式的put方法写入完成后，就需要调用flip翻转Buffer用于读取，可以理解成因为读写指针是同一个
        buf.flip();
        System.out.println("Remain after flip: " + buf.remaining());
        System.out.println(buf.get());
    }

    /**
     * 通过allocate / wrap创建一个Buffer，这两种方式都会有对应的底层Array，可以通过array / hasArray查看
     * 可以通过duplicate创建一个副本，拥有自己的position、limit、mark，但是和源Buffer共享数据
     */
    private static void allocateWrapDup() {
        ////// Allocate
        // 通过allocate和wrap创建的ByteBuffer，底层都有对应的Array
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("Allocate has array: " + buffer.hasArray());
        byte[] bytes = new byte[100];
        ByteBuffer buf = ByteBuffer.wrap(bytes, 10, 50);
        System.out.println(buf);

        ////// Wrap
        // 对于wrap创建的ByteBuffer，可以通过改变limit扩大/缩小访问界限
        buf.limit(100);
        System.out.println(buf);
        System.out.println(buf.hasArray()); // true，表示底层有对应的array
        System.out.println(buf.arrayOffset());  // 获取底层数组中第一个数据项的位置

        ///// Duplicate
        // duplicate创建的新的ByteBuffer有自己的position、limit、mark，但是和源ByteBuffer共享数据
        ByteBuffer dup = buffer.duplicate();
        // 对Duplicate创建的Buffer的修改会影响到源Buffer，因为他们share data
        dup.put(0, (byte) 10);
        System.out.println(buffer.get());
        // 创建read-only Buffer，写操作将抛出ReadOnlyBufferException
        ByteBuffer readOnlyBuffer = dup.asReadOnlyBuffer();
        readOnlyBuffer.put(1, (byte) 99);
    }

    /**
     * relative get/put和absolute get/put（参数带index），前者会改变position，后者不会
     */
    private static void getAndPut() {
        /// 含有index参数的put/get方法不会改变position的值，不含有index参数的会改变position的值
        ByteBuffer buf = ByteBuffer.allocate(10);
        /*
        可以看到第二个方法的调用改变了position的值
        java.nio.HeapByteBuffer[pos=0 lim=10 cap=10]
        java.nio.HeapByteBuffer[pos=1 lim=10 cap=10]
         */
        buf.put(4, (byte) 20);
        System.out.println(buf);
        buf.put(((byte) 4));
        System.out.println(buf);
    }
}
