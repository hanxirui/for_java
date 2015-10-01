package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

// 如果长度字段是提取框架的一部分,可以在LengthFieldBasedFrameDecoder的构造方法中配置,还可以指定提供的长度。
// FixedLengthFrameDecoder很容易使用,我们重点讲解LengthFieldBasedFrameDecoder。下面代码显示如何使用 LengthFieldBasedFrameDecoder提取8字节长度
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65 * 1024, 0, 8)).addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg) throws Exception {
        }
    }
}
