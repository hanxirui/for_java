package com.hxr.javatone.concurrency.netty.official.fundamental;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the echo client initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClient {

    private final String host;
    private final int port;
    private final int firstMessageSize;

    public EchoClient(final String host, final int port, final int firstMessageSize) {
        this.host = host;
        this.port = port;
        this.firstMessageSize = firstMessageSize;
    }

    public void run() throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(final SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                             //new LoggingHandler(LogLevel.INFO),
                             new EchoClientHandler(firstMessageSize));
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public static void main(final String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = Integer.parseInt("8080");
        // Print usage if no argument is specified.
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        // Parse options.

        final int firstMessageSize;
        if (args.length == 3) {
            firstMessageSize = Integer.parseInt(args[2]);
        } else {
            firstMessageSize = 256;
        }

        new EchoClient(host, port, firstMessageSize).run();
    }
}
