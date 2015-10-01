package com.hxr.javatone.concurrency.netty.inaction.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class FrameChunkDecoder extends ByteToMessageDecoder {
    // 限制大小
    private final int maxFrameSize;

    public FrameChunkDecoder(final int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        // 获取可读字节数
        int readableBytes = in.readableBytes();
        // 若可读字节数大于限制值,清空字节并抛出异常
        if (readableBytes > maxFrameSize) {
            in.clear();
            throw new TooLongFrameException();
        }
        // 读取ByteBuf并放到List中
        ByteBuf buf = in.readBytes(readableBytes);
        out.add(buf);
    }
}