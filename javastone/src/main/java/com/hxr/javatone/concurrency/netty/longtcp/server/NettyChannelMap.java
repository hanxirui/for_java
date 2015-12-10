package com.hxr.javatone.concurrency.netty.longtcp.server;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yaozb on 15-4-11.
 */
public class NettyChannelMap {
    private static Map<String,SocketChannel> map=new ConcurrentHashMap<String, SocketChannel>();
    public static void add(final String clientId,final SocketChannel socketChannel){
        map.put(clientId,socketChannel);
    }
    public static Channel get(final String clientId){
       return map.get(clientId);
    }
    public static void remove(final SocketChannel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
                map.remove(entry.getKey());
            }
        }
    }

}