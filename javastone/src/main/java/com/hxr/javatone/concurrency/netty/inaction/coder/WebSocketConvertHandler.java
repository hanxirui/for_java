package com.hxr.javatone.concurrency.netty.inaction.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

//Netty还提供了其他的协议支持,放在io.netty.handler.codec包下,如:
//Google的protobuf,在io.netty.handler.codec.protobuf包下
//Google的SPDY协议
//RTSP(Real Time Streaming Protocol,实时流传输协议),在io.netty.handler.codec.rtsp包下 
//SCTP(Stream Control Transmission Protocol,流控制传输协议),在io.netty.handler.codec.sctp包下
public class WebSocketConvertHandler extends
    MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {
    public static final WebSocketConvertHandler INSTANCE = new WebSocketConvertHandler();

    @Override
    protected void encode(final ChannelHandlerContext ctx, final MyWebSocketFrame msg, final List<Object> out) throws Exception {
        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(msg.getData()));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, msg.getData()));
                break;
            case PING:
                out.add(new PingWebSocketFrame(msg.getData()));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(msg.getData()));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(msg.getData()));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(msg.getData()));
                break;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, msg.content().copy()));
            return;
        }
        if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, msg.content().copy()));
            return;
        }
        if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, msg.content().copy()));
            return;
        }
        if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, msg.content().copy()));
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, msg.content().copy()));
            return;
        }
        if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, msg.content().copy()));
            return;
        }
        throw new IllegalStateException("Unsupported websocket msg " + msg);
    }

    public static final class MyWebSocketFrame {
        public enum FrameType {
            BINARY, CLOSE, PING, PONG, TEXT, CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(final FrameType type, final ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}
