package com.hxr.javatone.concurrency.netty.inaction.spdy;

import io.netty.handler.codec.spdy.SpdyOrHttpChooser;

import javax.net.ssl.SSLEngine;
  
public class DefaultSpdyOrHttpChooser extends SpdyOrHttpChooser {  
  
    protected DefaultSpdyOrHttpChooser(final int maxSpdyContentLength, final int maxHttpContentLength) {  
        super(maxSpdyContentLength, maxHttpContentLength);  
    }  
  
    @Override  
    protected SelectedProtocol getProtocol(final SSLEngine engine) {  
        DefaultServerProvider provider = (DefaultServerProvider) NextProtoNego  
                .get(engine);  
        String protocol = provider.getSelectedProtocol();  
        if (protocol == null) {  
            return SelectedProtocol.UNKNOWN;  
        }  
        switch (protocol) {  
        case "spdy/3.1":  
            return SelectedProtocol.SPDY_3_1;  
        case "http/1.0":  
        case "http/1.1":  
            return SelectedProtocol.HTTP_1_1;  
        default:  
            return SelectedProtocol.UNKNOWN;  
        }  
    }  
  
    @Override  
    protected ChannelInboundHandler createHttpRequestHandlerForHttp() {  
        return new HttpRequestHandler();  
    }  
  
    @Override  
    protected ChannelInboundHandler createHttpRequestHandlerForSpdy() {  
        return new SpdyRequestHandler();  
    }  
      
}  