package com.hxr.javatone.concurrency.netty.inaction.websocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.RandomAccessFile;

// 11.3.1 处理http请求
// 服务器将作为一种混合式以允许同时处理http和websocket,所以服务器还需要html页面,html用来充当客户端角色,连接服务器并
// 交互消息。因此,如果客户端不发送/ws的uri,我们需要写一个ChannelInboundHandler用来处理FullHttpRequest。看下面代码:
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    // websocket标识
    private final String wsUri;

    public HttpRequestHandler(final String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, final FullHttpRequest msg) throws Exception {
        // 如果是websocket请求,请求地址uri等于wsuri
        if (wsUri.equalsIgnoreCase(msg.uri())) {
            // 将消息转发到下一个ChannelHandler
            ctx.fireChannelRead(msg.retain());
        } else {
            // 如果不是websocket请求
            // 如果HTTP请求头部包含Expect: 100-continue,
            if (HttpHeaderUtil.is100ContinueExpected(msg)) {
                // 则响应请求
                FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }
            // 获取index.html的内容响应给客户端
            RandomAccessFile file =
                new RandomAccessFile(
                    "/Users/hanxirui/Documents/workspace/github/for_java/javastone/src/main/java/com/hxr/javatone/concurrency/netty/inaction/websocket/index.html",
                    "r");
            HttpResponse response = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            // 如果http请求保持活跃,设置http请求头部信息
            boolean keepAlive = HttpHeaderUtil.isKeepAlive(msg);
            // 并响应请求
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length() + "");
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION);
            }
            ctx.write(response); // 如果不是https请求,将index.html内容写入通道
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            // 标识响应内容结束并刷新通道
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                // 如果http请求不活跃,关闭http连接
                future.addListener(ChannelFutureListener.CLOSE);
            }
            file.close();
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
