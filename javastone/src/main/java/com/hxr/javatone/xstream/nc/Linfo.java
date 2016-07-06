package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Linfo {
    @XStreamAlias("H")
private String h;
    @XStreamAlias("T")
private String t;
    @XStreamAlias("DB")
private String db;
    @XStreamAlias("SC")
private String sc;
    @XStreamAlias("TNA")
private String tna;
    @XStreamAlias("LSC")
private String lsc;
    @XStreamAlias("LS")
private String ls;
    @XStreamAlias("U")
private String u;
    @XStreamAlias("P")
private String p;
    
    @XStreamAlias("LO")
    private String lo;
public String getLo() {
        return lo;
    }
    public void setLo(String lo) {
        this.lo = lo;
    }
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
public String getDb() {
    return db;
}
public void setDb(String db) {
    this.db = db;
}
public String getSc() {
    return sc;
}
public void setSc(String sc) {
    this.sc = sc;
}
public String getTna() {
    return tna;
}
public void setTna(String tna) {
    this.tna = tna;
}

public String getLsc() {
    return lsc;
}
public void setLsc(String lsc) {
    this.lsc = lsc;
}
public String getLs() {
    return ls;
}
public void setLs(String ls) {
    this.ls = ls;
}
public String getU() {
    return u;
}
public void setU(String u) {
    this.u = u;
}
public String getP() {
    return p;
}
public void setP(String p) {
    this.p = p;
}
}
