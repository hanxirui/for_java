package com.hxr.javatone.concurrency.netty.inaction.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class SecureChatServerIntializer extends ChatServerInitializer {
    private final SSLContext context;

    public SecureChatServerIntializer(final ChannelGroup group, final SSLContext context) {
        super(group);
        this.context = context;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        super.initChannel(ch);
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(false);
        ch.pipeline().addFirst(new SslHandler(engine));
    }
}
