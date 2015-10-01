package com.hxr.javatone.concurrency.netty.inaction.spdy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
  
public class DefaultServerProvider implements ServerProvider {  
  
    private static final List<String> PROTOCOLS = Collections.unmodifiableList(Arrays  
            .asList("spdy/3.1", "http/1.1", "http/1.0", "Unknown"));  
  
    private String protocol;  
  
    public String getSelectedProtocol() {  
        return protocol;  
    }  
  
    @Override  
    public void protocolSelected(final String arg0) {  
        this.protocol = arg0;  
    }  
  
    @Override  
    public List<String> protocols() {  
        return PROTOCOLS;  
    }  
  
    @Override  
    public void unsupported() {  
        protocol = "http/1.1";  
    }  
  
}  
