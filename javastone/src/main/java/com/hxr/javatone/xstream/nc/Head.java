package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Head {
    
    @XStreamAlias("NAME")
    private String name;
    @XStreamAlias("IP")
    private String ip;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
}
