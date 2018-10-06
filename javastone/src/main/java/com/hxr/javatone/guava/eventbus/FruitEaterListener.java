package com.hxr.javatone.guava.eventbus;

import com.google.common.eventbus.Subscribe;

public class FruitEaterListener {

    @Subscribe
    public void eat(Fruit fruit) throws RawFruitException{
        if (!fruit.isRipe()){
            System.out.println("Can't consume the mango, it is raw");
            throw new RawFruitException();
        }

        System.out.println("eat(Fruit " + fruit +")");

    }

    @Subscribe
    public void eat(Apple apple){
        System.out.println("eat(" + apple +")");
    }
}
