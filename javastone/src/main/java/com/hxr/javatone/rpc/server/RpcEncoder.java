package com.hxr.javatone.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
//使用 RpcEncoder 提供 RPC 编码，只需扩展 Netty 的 MessageToByteEncoder 抽象类的 encode 方法即可，代码如下：
public class RpcEncoder extends MessageToByteEncoder {
    private final Class<?> genericClass;
    public RpcEncoder(final Class<?> genericClass) {
      this.genericClass = genericClass;
    }
    @Override
    public void encode(final ChannelHandlerContext ctx, final Object in, final ByteBuf out) throws Exception {
      if (genericClass.isInstance(in)) {
        byte[] data = SerializationUtil.serialize(in);
        out.writeInt(data.length);
        out.writeBytes(data);
      }
    }
  }
