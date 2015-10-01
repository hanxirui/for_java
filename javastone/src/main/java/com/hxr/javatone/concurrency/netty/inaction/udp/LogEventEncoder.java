package com.hxr.javatone.concurrency.netty.inaction.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    
    private final InetSocketAddress remoteAddress;
    
    public LogEventEncoder(final InetSocketAddress remoteAddress){
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final LogEvent msg, final List<Object> out)
            throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(msg.getLogfile().getBytes(CharsetUtil.UTF_8));
        buf.writeByte(LogEvent.SEPARATOR);
        buf.writeBytes(msg.getMsg().getBytes(CharsetUtil.UTF_8));
        out.add(new DatagramPacket(buf, remoteAddress));
    }

}
