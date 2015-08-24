package com.hxr.javatone.rpc.simple;


public class Server {
    public static void main(final String []args) throws Exception {
        HelloServiceInterface service = new HelloServiceImpl();
        RpcFramework.export(service, 1234); 
    }
}
