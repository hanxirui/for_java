package com.hxr.javatone.xstream.nc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Body {

    @XStreamAlias("CINFO")
    private Cinfos cinfos;
    @XStreamAlias("CMS")
    private Cms cms;
    @XStreamAlias("DISC")
    private Discs discs;
    @XStreamAlias("SINFO")
    private Sinfos sinfos;
    @XStreamAlias("TS")
    private Tss tss;
    
    @XStreamAlias("LINFO")
    private Linfos linfos;

    public Linfos getLinfos() {
        return linfos;
    }

    public void setLinfos(final Linfos linfos) {
        this.linfos = linfos;
    }

    public Cinfos getCinfos() {
        return cinfos;
    }

    public void setCinfos(final Cinfos cinfos) {
        this.cinfos = cinfos;
    }

   

    public Discs getDiscs() {
        return discs;
    }

    public void setDiscs(final Discs discs) {
        this.discs = discs;
    }

    public Sinfos getSinfos() {
        return sinfos;
    }

    public void setSinfos(final Sinfos sinfos) {
        this.sinfos = sinfos;
    }

    public Tss getTss() {
        return tss;
    }

    public void setTss(final Tss tss) {
        this.tss = tss;
    }

    /**
     * @return cms - {return content description}
     */
    public Cms getCms() {
        return cms;
    }

    /**
     * @param cms - {parameter description}.
     */
    public void setCms(final Cms cms) {
        this.cms = cms;
    }
}
