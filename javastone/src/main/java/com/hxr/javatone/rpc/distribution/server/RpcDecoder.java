package com.hxr.javatone.rpc.distribution.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
//使用 RpcDecoder 提供 RPC 解码，只需扩展 Netty 的 ByteToMessageDecoder 抽象类的 decode 方法即可，代码如下：
public class RpcDecoder extends ByteToMessageDecoder {
    private final Class<?> genericClass;
    public RpcDecoder(final Class<?> genericClass) {
      this.genericClass = genericClass;
    }
    @Override
    public final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
      if (in.readableBytes() < 4) {
        return;
      }
      in.markReaderIndex();
      int dataLength = in.readInt();
      if (dataLength < 0) {
        ctx.close();
      }
      if (in.readableBytes() < dataLength) {
        in.resetReaderIndex();
      }
      byte[] data = new byte[dataLength];
      in.readBytes(data);
      Object obj = SerializationUtil.deserialize(data, genericClass);
      out.add(obj);
    }
  }