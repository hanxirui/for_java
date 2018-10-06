package com.hxr.javatone.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class HandleNotAfruitException implements SubscriberExceptionHandler {

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context){
        if (exception instanceof  RawFruitException){
            Fruit fruit = (Fruit) context.getEvent();
            fruit.setRipe(true);

            EventBus eventBus = context.getEventBus();
            eventBus.post(fruit);
        }
    }
}
