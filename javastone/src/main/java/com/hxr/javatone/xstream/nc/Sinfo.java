package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Sinfo {
    @XStreamAlias("S")
    private String s;
    @XStreamAlias("N")
    private String n;
    @XStreamAlias("HN")
    private String hn;
    @XStreamAlias("DB")
    private String db;
    @XStreamAlias("P")
    private String p;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
