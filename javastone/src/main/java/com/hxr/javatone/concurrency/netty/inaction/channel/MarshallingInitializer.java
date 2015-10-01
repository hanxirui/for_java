package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

// 如果你想使用外部依赖的接口,JBoss编组是个好方法。JBoss Marshalling序列化的速度是JDK的3倍,并且序列化的结构更紧凑,从而
// 使序列化后的数据更小。Netty附带了JBoss编组序列化的实现,这些实现接口放在io.netty.handler.codec.marshalling包下面:
// CompatibleMarshallingEncoder CompatibleMarshallingDecoder MarshallingEncoder MarshallingDecoder
public class MarshallingInitializer extends ChannelInitializer<Channel> {
    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public MarshallingInitializer(final MarshallerProvider marshallerProvider,
        final UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new MarshallingDecoder(unmarshallerProvider))
            .addLast(new MarshallingEncoder(marshallerProvider)).addLast(new ObjectHandler());
    }

    public final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {
        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final Serializable msg) throws Exception {
            // do something
        }

    }
}
