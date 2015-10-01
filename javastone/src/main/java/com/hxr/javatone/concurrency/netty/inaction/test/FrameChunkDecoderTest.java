package com.hxr.javatone.concurrency.netty.inaction.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;

import org.junit.Assert;
import org.junit.Test;

public class FrameChunkDecoderTest {
    @Test
    public void testFramesDecoded() {
        // 创建ByteBuf并填充9字节数据
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        // 复制一个ByteBuf
        ByteBuf input = buf.duplicate();
        // 创建EmbeddedChannel
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
        // 读取2个字节写入入站通道
        Assert.assertTrue(channel.writeInbound(input.readBytes(2)));
        try {
            // 读取4个字节写入入站通道
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {

        }
        // 读取3个字节写入入站通道
        Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
        // 标识完成
        Assert.assertTrue(channel.finish());
        // 从EmbeddedChannel入去入站数据
        Assert.assertEquals(buf.readBytes(2), channel.readInbound());
        Assert.assertEquals(buf.skipBytes(4).readBytes(3), channel.readInbound());
    }
}
