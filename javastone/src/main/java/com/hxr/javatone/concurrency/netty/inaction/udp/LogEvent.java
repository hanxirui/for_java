package com.hxr.javatone.concurrency.netty.inaction.udp;


import java.net.InetSocketAddress;

public class LogEvent {

    public static final byte SEPARATOR = (byte) '|';

    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    public LogEvent(final String logfile, final String msg) {
        this(null, -1, logfile, msg);
    }

    public LogEvent(final InetSocketAddress source, final long received, final String logfile, final String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }

}
