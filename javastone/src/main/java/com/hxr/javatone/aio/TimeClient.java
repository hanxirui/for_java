package com.hxr.javatone.aio;

public class TimeClient {
public static void main(final String[] args) {
    new Thread(new AsyncTimeClientHandler("127.0.0.1",8090),"asyncTimeClient").start();
}
}
