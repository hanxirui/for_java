package com.hxr.javatone.concurrency.netty.inaction.spdy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;
  
public class SpdyServer {  
  
    private final NioEventLoopGroup group = new NioEventLoopGroup();  
    private final SSLContext context;  
    private Channel channel;  
  
    public SpdyServer(final SSLContext context) {  
        this.context = context;  
    }  
  
    public ChannelFuture start(final InetSocketAddress address) {  
        ServerBootstrap bootstrap = new ServerBootstrap();  
        bootstrap.group(group).channel(NioServerSocketChannel.class)  
                .childHandler(new SpdyChannelInitializer(context));  
        ChannelFuture future = bootstrap.bind(address);  
        future.syncUninterruptibly();  
        channel = future.channel();  
        return future;  
    }  
  
    public void destroy() {  
        if (channel != null) {  
            channel.close();  
        }  
        group.shutdownGracefully();  
    }  
  
    public static void main(final String[] args) {  
        SSLContext context = SecureChatSslContextFactory.getServerContext();  
        final SpdyServer endpoint = new SpdyServer(context);  
        ChannelFuture future = endpoint.start(new InetSocketAddress(4096));  
        Runtime.getRuntime().addShutdownHook(new Thread() {  
            @Override  
            public void run() {  
                endpoint.destroy();  
            }  
        });  
        future.channel().closeFuture().syncUninterruptibly();  
    }  
  
}  
