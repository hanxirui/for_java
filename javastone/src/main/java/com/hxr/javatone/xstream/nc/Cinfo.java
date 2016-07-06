package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Cinfo {
    
    @XStreamAlias("N")
  private String n;
    @XStreamAlias("P")
  private String p;
    @XStreamAlias("PS")
  private String ps;
    @XStreamAlias("AHR")
  private String ahr;
    @XStreamAlias("DHR")
  private String dhr;
    @XStreamAlias("IHR")
  private String ihr;
    @XStreamAlias("SHR")
  private String shr;
    @XStreamAlias("RUT")
  private String rut;
public String getN() {
    return n;
}
public void setN(String n) {
    this.n = n;
}
public String getP() {
    return p;
}
public void setP(String p) {
    this.p = p;
}
public String getPs() {
    return ps;
}
public void setPs(String ps) {
    this.ps = ps;
}
public String getAhr() {
    return ahr;
}
public void setAhr(String ahr) {
    this.ahr = ahr;
}
public String getDhr() {
    return dhr;
}
public void setDhr(String dhr) {
    this.dhr = dhr;
}
public String getIhr() {
    return ihr;
}
public void setIhr(String ihr) {
    this.ihr = ihr;
}
public String getShr() {
    return shr;
}
public void setShr(String shr) {
    this.shr = shr;
}
public String getRut() {
    return rut;
}
public void setRut(String rut) {
    this.rut = rut;
}
}
