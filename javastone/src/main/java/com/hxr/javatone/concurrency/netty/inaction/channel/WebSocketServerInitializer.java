package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * HTTP是不错的协议,但是如果需要实时发布信息怎么做?有个做法就是客户端一直轮询请求服务器,这种方式虽然可以达到目的,但是 其缺点很多,也不是优秀的解决方案,为了解决这个问题,便出现了WebSocket。
 * WebSocket允许数据双向传输,而不需要请求-响应模式。早期的WebSocket只能发送文本数据,然后现在不仅可以发送文本数据,也 可以发送二进制数据,这使得可以使用WebSocket构建你想要的程序。 WebSocket
 * Server,若想使用SSL加密,将SslHandler加载ChannelPipeline的最前面即可
 * 
 * @author c.k
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec(), new HttpObjectAggregator(65536),
            new WebSocketServerProtocolHandler("/websocket"), new TextFrameHandler(), new BinaryFrameHandler(),
            new ContinuationFrameHandler());
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final TextWebSocketFrame msg) throws Exception {
            // handler text frame

        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final BinaryWebSocketFrame msg)
            throws Exception {
            // handler binary frame

        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final ContinuationWebSocketFrame msg)
            throws Exception {
            // handler continuation frame

        }

    }
}
