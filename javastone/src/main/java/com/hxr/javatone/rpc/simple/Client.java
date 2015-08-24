package com.hxr.javatone.rpc.simple;



public class Client {
    public static void main(final String[] args) throws Exception {  
        HelloServiceInterface service = RpcFramework.refer(HelloServiceInterface.class, "127.0.0.1", 1234);  
        for (int i = 0; i < Integer.MAX_VALUE; i ++) {  
            String hello = service.hello("World" + i);  
            System.out.println(hello);  
            Thread.sleep(1000);  
        }  
    }  
}
