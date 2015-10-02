package com.hxr.javatone.concurrency.netty.inaction.Memcached;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * test memcached decoder
 * 
 * @author c.king
 * 
 */
public class MemcachedResponseDecoderTest {

    @Test
    public void testMemcachedResponseDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new MemcachedResponseDecoder());
        byte magic = 1;
        byte opCode = Opcode.SET;
        byte dataType = 0;
        byte[] key = "Key1".getBytes(CharsetUtil.UTF_8);
        byte[] body = "Value".getBytes(CharsetUtil.UTF_8);
        int id = (int) System.currentTimeMillis();
        long cas = System.currentTimeMillis();
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(magic);
        buffer.writeByte(opCode);
        buffer.writeShort(key.length);
        buffer.writeByte(0);
        buffer.writeByte(dataType);
        buffer.writeShort(Status.KEY_EXISTS);
        buffer.writeInt(body.length + key.length);
        buffer.writeInt(id);
        buffer.writeLong(cas);
        buffer.writeBytes(key);
        buffer.writeBytes(body);
        Assert.assertTrue(channel.writeInbound(buffer));
        MemcachedResponse response = (MemcachedResponse) channel.readInbound();
        assertResponse(response, magic, opCode, dataType, Status.KEY_EXISTS, 0,
                0, id, cas, key, body);
    }

    private static void assertResponse(final MemcachedResponse response, final byte magic,
            final byte opCode, final byte dataType, final short status, final int expires, final int flags,
            final int id, final long cas, final byte[] key, final byte[] body) {
        Assert.assertEquals(magic, response.getMagic());
        Assert.assertArrayEquals(key,
                response.getKey().getBytes(CharsetUtil.UTF_8));
        Assert.assertEquals(opCode, response.getOpCode());
        Assert.assertEquals(dataType, response.getDataType());
        Assert.assertEquals(status, response.getStatus());
        Assert.assertEquals(cas, response.getCas());
        Assert.assertEquals(expires, response.getExpires());
        Assert.assertEquals(flags, response.getFlags());
        Assert.assertArrayEquals(body,
                response.getData().getBytes(CharsetUtil.UTF_8));
        Assert.assertEquals(id, response.getId());
    }

}
