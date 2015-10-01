package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SSLContext context;
    private final boolean client;
    private final boolean startTls;

    public SslChannelInitializer(final SSLContext context, final boolean client, final boolean startTls) {
        this.context = context;
        this.client = client;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(client);
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }

}
