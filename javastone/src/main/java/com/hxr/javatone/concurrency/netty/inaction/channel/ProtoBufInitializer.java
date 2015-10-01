package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import java.io.Serializable;

import com.google.protobuf.MessageLite;

// 有一个序列化方案是Netty附带的ProtoBuf。protobuf是Google开源的一种编码和解码技术,它的作用是使序列化数据更高效。并且谷
// 歌提供了protobuf的不同语言的实现,所以protobuf在跨平台项目中是非常好的选择。Netty附带的protobuf放在io.netty.handler.codec.protobuf 包下面:
// ProtobufDecoder
// ProtobufEncoder ProtobufVarint32FrameDecoder ProtobufVarint32LengthFieldPrepender
public class ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite;

    public ProtoBufInitializer(final MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder()).addLast(new ProtobufEncoder())
            .addLast(new ProtobufDecoder(lite)).addLast(new ObjectHandler());
    }

    public final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {
        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final Serializable msg) throws Exception {
        }
    }
}
