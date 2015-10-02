package com.hxr.javatone.concurrency.netty.inaction.Memcached;

import java.util.Random;

/**
 * memcached request message object
 * @author c.king
 *
 */
public class MemcachedRequest {

    private static final Random rand = new Random();
    private final int magic = 0x80;// fixed so hard coded
    private final byte opCode; // the operation e.g. set or get
    private final String key; // the key to delete, get or set
    private final int flags = 0xdeadbeef; // random
    private int expires; // 0 = item never expires
    private final String body; // if opCode is set, the value
    private final int id = rand.nextInt(); // Opaque
    private long cas; // data version check...not used
    private final boolean hasExtras; // not all ops have extras

    public MemcachedRequest(final byte opcode, final String key, final String value) {
        this.opCode = opcode;
        this.key = key;
        this.body = value == null ? "" : value;
        // only set command has extras in our example
        hasExtras = opcode == Opcode.SET;
    }

    public MemcachedRequest(final byte opCode, final String key) {
        this(opCode, key, null);
    }

    public int getMagic() {
        return magic;
    }

    public byte getOpCode() {
        return opCode;
    }

    public String getKey() {
        return key;
    }

    public int getFlags() {
        return flags;
    }

    public int getExpires() {
        return expires;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    public long getCas() {
        return cas;
    }

    public boolean isHasExtras() {
        return hasExtras;
    }

}
