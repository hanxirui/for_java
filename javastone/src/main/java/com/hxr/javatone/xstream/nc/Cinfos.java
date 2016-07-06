package com.hxr.javatone.xstream.nc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Cinfos {
    @XStreamImplicit(itemFieldName="TN")
    private List<Cinfo> items;

    public List<Cinfo> getItems() {
        return items;
    }

    public void setItems(List<Cinfo> items) {
        this.items = items;
    }
}
