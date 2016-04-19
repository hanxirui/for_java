package com.hxr.javatone.io.aio;

public class TimeServer {
public static void main(final String[] args) {
    int port = 8090;
    
    AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
    new Thread(timeServer,"AIO-timeserver").start();
}
}
