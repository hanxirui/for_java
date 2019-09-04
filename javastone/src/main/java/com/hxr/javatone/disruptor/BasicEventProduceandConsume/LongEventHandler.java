package com.hxr.javatone.disruptor.BasicEventProduceandConsume;

import com.lmax.disruptor.EventHandler;

/**
 * Event consumer
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(sequence+" :: "+"Event: "+longEvent.getValue()+" endOfBatch: "+endOfBatch);
    }
}
