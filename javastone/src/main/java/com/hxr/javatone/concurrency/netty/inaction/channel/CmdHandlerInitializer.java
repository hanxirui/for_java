package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

//如果框架的东西除了换行符还有别的分隔符,可以使用DelimiterBasedFrameDecoder,只需要将分隔符传递到构造方法中。如果想实现 自己的以分隔符为基础的协议,这些解码器是有用的。例如,现在有个协议,它只处理命令,这些命令由名称和参数形成,名称和参数由一 个空格分隔,实现这个需求的代码如下:
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new CmdDecoder(65 * 1024), new CmdHandler());
    }

    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(final ByteBuf name, final ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        /**
         * @return name - {return content description}
         */
        public ByteBuf getName() {
            return name;
        }

        /**
         * @return args - {return content description}
         */
        public ByteBuf getArgs() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(final int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                return null;
            }

            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), (byte) ' ');
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, final Cmd msg) throws Exception {
            // do something with the command
        }
    }

}
