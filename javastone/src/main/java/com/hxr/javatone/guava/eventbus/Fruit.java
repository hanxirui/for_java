package com.hxr.javatone.guava.eventbus;

public class Fruit {
    private String name;
    private boolean ripe;

    public Fruit(String name) {
        this.name = name;
        this.ripe = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRipe() {
        return ripe;
    }

    public void setRipe(boolean ripe) {
        this.ripe = ripe;
    }

    @Override
    public String toString() {
        return
                "name=" + name;
    }
}
