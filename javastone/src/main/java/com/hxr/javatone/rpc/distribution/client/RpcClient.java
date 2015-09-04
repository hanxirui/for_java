package com.hxr.javatone.rpc.distribution.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxr.javatone.rpc.distribution.server.RpcDecoder;
import com.hxr.javatone.rpc.distribution.server.RpcEncoder;
import com.hxr.javatone.rpc.distribution.server.RpcRequest;
import com.hxr.javatone.rpc.distribution.server.RpcResponse;

//使用 RpcClient 类实现 RPC 客户端，只需扩展 Netty 提供的 SimpleChannelInboundHandler 抽象类即可，代码如下：
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
    private final String host;
    private final int port;
    private RpcResponse response;
    private final Object obj = new Object();
    public RpcClient(final String host, final int port) {
      this.host = host;
      this.port = port;
    }
//    @Override netty 3.0
    public void channelRead0(final ChannelHandlerContext ctx, final RpcResponse response) throws Exception {
      this.response = response;
      synchronized (obj) {
        obj.notifyAll(); // 收到响应，唤醒线程
      }
    }
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
      LOGGER.error("client caught exception", cause);
      ctx.close();
    }
    public RpcResponse send(final RpcRequest request) throws Exception {
      EventLoopGroup group = new NioEventLoopGroup();
      try {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(final SocketChannel channel) throws Exception {
              channel.pipeline()
                .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
            }
          })
          .option(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = bootstrap.connect(host, port).sync();
        future.channel().writeAndFlush(request).sync();
        synchronized (obj) {
          obj.wait(); // 未收到响应，使线程等待
        }
        if (response != null) {
          future.channel().closeFuture().sync();
        }
        return response;
      } finally {
        group.shutdownGracefully();
      }
    }
    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, final RpcResponse response) throws Exception {

        this.response = response;
        synchronized (obj) {
          obj.notifyAll(); // 收到响应，唤醒线程
        }
      
        
    }
  }
