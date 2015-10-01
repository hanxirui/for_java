package com.hxr.javatone.concurrency.netty.inaction.buffers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

public class Buffers {
    public static void main(final String[] args) {
//        directBuf();

//        compBuf();

//        Derived buffers
//        get a Charset of UTF-8
        Charset utf8 = Charset.forName("UTF-8");
        
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action 真棒。", utf8);
        
//        slice
        ByteBuf sliced = buf.slice(0, 14);
        
//        copy
        ByteBuf copy = buf.copy(0,14);
        
        System.out.println(buf.toString(utf8));
        System.out.println(sliced.toString(utf8));
        System.out.println(sliced.toString(utf8));
    }

    /**
     * {method description}. --
     */
    private static void compBuf() {
        // Composite Buffer
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        ByteBuf heapBuf = Unpooled.buffer(8);
        ByteBuf directBuf = Unpooled.directBuffer(16);
        // 添加ByteBuf到CompositeByteBuf
        compBuf.addComponents(heapBuf, directBuf);
        // 删除第一个ByteBuf

        // compBuf.removeComponent(0);
        Iterator<ByteBuf> iter = compBuf.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().toString());
        }

        // 随机访问索引
        for (int i = 0; i < 8; i++) {
            heapBuf.writeByte(i + 1);
        }

        //
        for (int i = 0; i < heapBuf.capacity(); i++) {
            System.out.print(heapBuf.getByte(i)+",");
        }
        System.out.println();
        
//        顺序读取
        while(heapBuf.isReadable()){
            System.out.print(heapBuf.readByte()+",");
        }
        System.out.println();

        Random random = new Random();
//        内容写入
        while(directBuf.writableBytes()>=4){
            directBuf.writeInt(random.nextInt());
        }
        while(directBuf.isReadable()){
            System.out.print(directBuf.readInt()+",");
            
        }
        System.out.println();
    }

    /**
     * {method description}. --
     */
    private static void directBuf() {
        // Direct Buffer
        // 直接缓冲区不支持数组访问数据，但是我们可以间接的访问数据数组
        ByteBuf directBuf = Unpooled.directBuffer(16);
        if (!directBuf.hasArray()) {
            int len = directBuf.readableBytes();
            byte[] arr = new byte[len];
            directBuf.getBytes(0, arr);
            System.out.println(arr);
        }
    }
}
