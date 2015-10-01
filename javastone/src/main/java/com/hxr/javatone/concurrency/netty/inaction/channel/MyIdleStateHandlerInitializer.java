package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

// An example that sends a HEARTBEAT message when there is no outbound traffic
// for 30 seconds.  The connection is closed when there is no inbound traffic
// for 60 seconds.
//* {@link ServerBootstrap} bootstrap = ...;
//* ...
//* bootstrap.childHandler(new MyIdleStateHandlerInitializer());

public class MyIdleStateHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    public void initChannel(final Channel channel) {
        channel.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 30, 0));
        channel.pipeline().addLast("myHandler", new MyHandler());
    }
    
 // Handler should handle the IdleStateEvent triggered by IdleStateHandler.
    public class MyHandler extends ChannelHandlerAdapter {
        private final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
            "HEARTBEAT", CharsetUtil.UTF_8));
        
        @Override
        public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    ctx.close();
                } else if (e.state() == IdleState.WRITER_IDLE) {
                    ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }
    }
}

