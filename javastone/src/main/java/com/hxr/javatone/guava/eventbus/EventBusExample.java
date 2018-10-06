package com.hxr.javatone.guava.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusExample {
    public static void main(String[] args){
        EventBus eventBus = new EventBus(new HandleNotAfruitException());
        eventBus.register(new FruitEaterListener());

//        eventBus.post(new Apple());

        eventBus.post(new Fruit("Orange"));
    }
}
