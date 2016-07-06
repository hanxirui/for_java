package com.hxr.javatone.xstream.nc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Tss {
    @XStreamImplicit(itemFieldName="TN")
    private List<Ts> items;

    public List<Ts> getItems() {
        return items;
    }

    public void setItems(List<Ts> items) {
        this.items = items;
    }
}
