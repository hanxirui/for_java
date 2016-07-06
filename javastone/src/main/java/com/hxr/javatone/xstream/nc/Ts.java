package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Ts {
    @XStreamAlias("H")
    private String h;
    @XStreamAlias("T")
    private String t;
    @XStreamAlias("A")
    private String a;
    @XStreamAlias("U")
    private String u;

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }
}
