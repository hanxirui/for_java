package com.hxr.javatone.xstream.nc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Sinfos {
    @XStreamImplicit(itemFieldName="TN")
    private List<Sinfo> items;

    public List<Sinfo> getItems() {
        return items;
    }

    public void setItems(List<Sinfo> items) {
        this.items = items;
    }
}
