package com.hxr.javatone.concurrency.netty.inaction.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * 访问地址:http://localhost:2048
 *
 * @author c.k
 */
public class ChatServer {
    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(final InetSocketAddress address) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(group));
        ChannelFuture f = b.bind(address).syncUninterruptibly();
        channel = f.channel();
        return f;
    }

    public void destroy() {
        if (channel != null)
            channel.close();
        group.close();
        workerGroup.shutdownGracefully();
    }

    protected ChannelInitializer<Channel> createInitializer(final ChannelGroup group) {
        return new ChatServerInitializer(group);
    }

    public static void main(final String[] args) {
        final ChatServer server = new ChatServer();
        ChannelFuture f = server.start(new InetSocketAddress(2048));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.destroy();
            }
        });
        f.channel().closeFuture().syncUninterruptibly();
    }
}
