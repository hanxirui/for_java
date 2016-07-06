package com.hxr.javatone.xstream.nc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Linfos {
    @XStreamImplicit(itemFieldName="TN")
    private List<Linfo> items;

    public List<Linfo> getItems() {
        return items;
    }

    public void setItems(List<Linfo> items) {
        this.items = items;
    }
}
