package com.hxr.javatone.temp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanxirui on 2017/6/29.
 */
public class Department {
    private String id;
    private String name;
    private List<Department> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }
}
