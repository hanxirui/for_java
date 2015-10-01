package com.hxr.javatone.concurrency.netty.inaction.spdy;

public class SpdyRequestHandler extends HttpRequestHandler {  
    
    @Override  
    protected String getContent() {  
        return "This content is transmitted via SPDY\r\n";  
    }  
      
}  