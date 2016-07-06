package com.hxr.javatone.xstream.nc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Discs {
    @XStreamImplicit(itemFieldName="TN")
    private List<Disc> items;

    public List<Disc> getItems() {
        return items;
    }

    public void setItems(List<Disc> items) {
        this.items = items;
    }
}
