package com.hxr.javatone.concurrency.netty.inaction.Memcached;


/**
 * memcached response message object
 * @author c.king
 *
 */
public class MemcachedResponse {

    private final byte magic;
    private final byte opCode;
    private final byte dataType;
    private final short status;
    private final int id;
    private final long cas;
    private final int flags;
    private final int expires;
    private final String key;
    private final String data;

    public MemcachedResponse(final byte magic, final byte opCode, final byte dataType, final short status,
            final int id, final long cas, final int flags, final int expires, final String key, final String data) {
        this.magic = magic;
        this.opCode = opCode;
        this.dataType = dataType;
        this.status = status;
        this.id = id;
        this.cas = cas;
        this.flags = flags;
        this.expires = expires;
        this.key = key;
        this.data = data;
    }

    public byte getMagic() {
        return magic;
    }

    public byte getOpCode() {
        return opCode;
    }

    public byte getDataType() {
        return dataType;
    }

    public short getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public long getCas() {
        return cas;
    }

    public int getFlags() {
        return flags;
    }

    public int getExpires() {
        return expires;
    }

    public String getKey() {
        return key;
    }

    public String getData() {
        return data;
    }

}

