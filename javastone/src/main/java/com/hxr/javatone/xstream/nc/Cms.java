package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Cms {
    @XStreamAlias("C")
    private String c;
    @XStreamAlias("MA")
    private String ma;
    @XStreamAlias("M")
    private String m;
    @XStreamAlias("SA")
    private String sa;
    @XStreamAlias("S")
    private String s;

    public String getC() {
        return c;
    }

    public void setC(final String c) {
        this.c = c;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(final String ma) {
        this.ma = ma;
    }

    public String getM() {
        return m;
    }

    public void setM(final String m) {
        this.m = m;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(final String sa) {
        this.sa = sa;
    }

    public String getS() {
        return s;
    }

    public void setS(final String s) {
        this.s = s;
    }
}
