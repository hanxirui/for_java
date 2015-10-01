package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;

    public ChunkedWriteHandlerInitializer(final File file) {
        this.file = file;
    }

    // 如果只想发送文件中指定的数据块应该怎么做呢?
    // Netty提供了ChunkedWriteHandler,允许通过处理ChunkedInput来写大的数据块。下 面是ChunkedInput的一些实现类:
    // ChunkedFile ChunkedNioFile ChunkedStream ChunkedNioStream
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new ChunkedWriteHandler()).addLast(new WriteStreamHandler());
    }

    public final class WriteStreamHandler extends ChannelHandlerAdapter {
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }

        // 写大量的数据的一个有效的方法是使用异步框架,如果内存和网络都处于饱满负荷状态,你需要停止写,否则会报OutOfMemoryError。
        // Netty提供了写文件内容时zero-memory-copy机制,这种方法再将文件内容写到网络堆栈空间时可以获得最大的性能。使用零拷贝写文件的内
        // 容时通过DefaultFileRegion、ChannelHandlerContext、ChannelPipeline,看下面代码:
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            File file = new File("test.txt");
            FileInputStream fis = new FileInputStream(file);
            FileRegion region = new DefaultFileRegion(fis.getChannel(), 0, file.length());
            Channel channel = ctx.channel();
            channel.writeAndFlush(region).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        Throwable cause = future.cause();
                        // do something
                    }
                }
            });
        }
    }
}
