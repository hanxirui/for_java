package com.hxr.javatone.concurrency.netty.inaction.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

// 有时候需要从另一个Channel引导客户端,例如写一个代理或需要从其他系统检索数据。从其他系统获取数据时比较常见的,有很
// 多Netty应用程序必须要和企业现有的系统集成,如Netty程序与内部系统进行身份验证,查询数据库等。 当然,你可以创建一个新的引导,这样做没有什么不妥,只是效率不高,因为要为新创建的客户端通道使用另一个EventLoop,如
// 果需要在已接受的通道和客户端通道之间交换数据则需要切换上下文线程。Netty对这方面进行了优化,可以讲已接受的通道通过
// eventLoop(...)传递到EventLoop,从而使客户端通道在相同的EventLoop里运行。这消除了额外的上下文切换工作,因为EventLoop继承
// 于EventLoopGroup。除了消除上下文切换,还可以在不需要创建多个线程的情况下使用引导。
// 为什么要共享EventLoop呢?一个EventLoop由一个线程执行,共享EventLoop可以确定所有的Channel都分配给同一线程的 EventLoop,这样就避免了不同线程之间切换上下文,从而减少资源开销。
/**
 * {class description} <br>
 * <p>
 * Create on : 2015年10月1日<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author hanxirui<br>
 * @version javastone v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class BootstrapingFromChannel {

    public static void main(final String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                ChannelFuture anotherConnectFuture;

                @Override
                public void channelActive(final ChannelHandlerContext ctx) throws Exception {
                    Bootstrap anotherBootstrap = new Bootstrap();
                    anotherBootstrap.channel(NioSocketChannel.class).handler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg)
                                throws Exception {
                                System.out.println("Received data");
                                msg.clear();
                            }
                        });
                    anotherBootstrap.group(ctx.channel().eventLoop());
                    anotherConnectFuture = anotherBootstrap.connect(new InetSocketAddress("127.0.0.1", 2048));
                }

                @Override
                protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg) throws Exception {
                    if (anotherConnectFuture.isDone()) {
                        // do something with the data
                    }
                }
            });
        ChannelFuture f = b.bind(2048);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {

                if (future.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("bound fail");
                    future.cause().printStackTrace();
                }
            }
        });
    }

    // 比较麻烦的是创建通道后不得不手动配置每个通道,为了避免这种情况,Netty提供了ChannelOption来帮助引导配置。这些选项会
    // 自动应用到引导创建的所有通道,可用的各种选项可以配置底层连接的详细信息,如通道“keep-alive(保持活跃)”或“timeout(超时)”的特 性。
    // ￼
    // Netty应用程序通常会与组织或公司其他的软件进行集成,在某些情况下,Netty的组件如通道、传递和Netty正常生命周期外使用;
    // 在这样的情况下并不是所有的一般属性和数据时可用的。这只是一个例子,但在这样的情况下,Netty提供了通道属性(channel attributes)。
    // 属性可以将数据和通道以一个安全的方式关联,这些属性只是作用于客户端和服务器的通道。例如,例如客户端请求web服务器应
    // 用程序,为了跟踪通道属于哪个用户,应用程序可以存储用的ID作为通道的一个属性。任何对象或数据都可以使用属性被关联到一个通 道。
    // 使用ChannelOption和属性可以让事情变得很简单,例如Netty WebSocket服务器根据用户自动路由消息,通过使用属性,应用程序
    // 能在通道存储用户ID以确定消息应该发送到哪里。应用程序可以通过使用一个通道选项进一步自动化,给定时间内没有收到消息将自动 断开连接。看下面代码:
    public static void diff_main(final String[] args) {
        // 创建属性键对象
        final AttributeKey<Integer> idAttr = AttributeKey.valueOf("ID");
        // 客户端引导对象
        Bootstrap b = new Bootstrap();
        // 设置EventLoop,设置通道类型
        b.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
        // 设置ChannelHandler
            .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg) throws Exception {
                    System.out.println("Reveived data");
                    msg.clear();
                }

                @Override
                public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
                    // 通道注册后执行,获取属性值
                    Integer idValue = ctx.channel().attr(idAttr).get();
                    System.out.println(idValue);
                    // do something with the idValue
                }
            });
        // 设置通道选项,在通道注册后或被创建后设置
        b.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        // 设置通道属性
        b.attr(idAttr, 123456);
        ChannelFuture f = b.connect("www.manning.com", 80);
        f.syncUninterruptibly();
    }

}
