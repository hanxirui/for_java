package com.hxr.javatone.concurrency.netty.inaction.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpAggregatorInitializer(final boolean client) {
        this.client = client;
    }

//    http消息聚合
//    @Override
//    protected void initChannel(final Channel ch) throws Exception {
//        ChannelPipeline pipeline = ch.pipeline();
//        if (client) {
//            pipeline.addLast("codec", new HttpClientCodec());
//        } else {
//            pipeline.addLast("codec", new HttpServerCodec());
//        }
//        pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
//    }
    
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            // 添加解压缩Handler
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            // 添加解压缩Handler
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        }
        pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
    }
}
