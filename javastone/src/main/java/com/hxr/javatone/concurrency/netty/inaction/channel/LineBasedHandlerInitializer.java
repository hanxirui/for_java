package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

//解码分隔符和基于长度的协议
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new LineBasedFrameDecoder(65 * 1204), new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg) throws Exception {
            // do something with the frame
        }
    }
}
