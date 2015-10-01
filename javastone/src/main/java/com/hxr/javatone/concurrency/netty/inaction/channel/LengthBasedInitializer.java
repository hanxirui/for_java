package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

// 如果长度字段是提取框架的一部分,可以在LengthFieldBasedFrameDecoder的构造方法中配置,还可以指定提供的长度。
// FixedLengthFrameDecoder很容易使用,我们重点讲解LengthFieldBasedFrameDecoder。
//下面代码显示如何使用 LengthFieldBasedFrameDecoder提取8字节长度
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65 * 1024, 0, 8)).addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final ByteBuf msg) throws Exception {
        }
    }
}


//我们接下来看一个也是比较重要的的解码器LengthFieldBasedFrameDecoder，这个和DelimiterBasedFrameDecoder比起来没有那么难理解，所以我们简单的看一下。
//
//和之前一样，我们先来看一下局部变量。
//private final int maxFrameLength;  
//private final int lengthFieldOffset;  
//private final int lengthFieldLength;  
//private final int lengthFieldEndOffset;  
//private final int lengthAdjustment;  
//private final int initialBytesToStrip;  
//private final boolean failFast;  
//private boolean discardingTooLongFrame;  
//private long tooLongFrameLength;  
//private long bytesToDiscard;  

//maxFrameLength 这个定义最大帧的长度
//lengthFieldOffset 长度属性的起始指针(偏移量)
//lengthFieldLength 长度属性的长度，即存放数据包长度的变量的的字节所占的长度
//lengthFieldEndOffset 这个是一个快捷属性，是根据lengthFieldOffset和lengthFieldLength计算出来的，即就是起始偏移量+长度=结束偏移量
//lengthAdjustment 这个是一个长度调节值，例如当总长包含头部信息的时候，这个可以是个负数，就比较好实现了
//initialBytesToStrip 这个属性也比较好理解，就是解码后的数据包需要跳过的头部信息的字节数
//failFast 这个和DelimiterBasedFrameDecoder是一致的，就是如果设置成true，当发现解析的数据超过maxFrameLenght就立马报错，否则当整个帧的数据解析完后才报错
//discardingTooLongFrame 这个也是一个导出属性，就是当前编码器的状态，是不是处于丢弃超长帧的状态
//tooLongFrameLength 这个是当出现超长帧的时候，这个超长帧的长度
//bytesToDiscard 这个来定义，当出现超长帧的时候，丢弃的数据的字节数
