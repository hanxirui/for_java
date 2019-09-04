package com.hxr.javatone.disruptor.BasicEventProduceandConsume;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
//        Grab the next sequence
        long sequence = ringBuffer.next();
        try {
//            Get the entry in the Disruptor for the sequence
            LongEvent event = ringBuffer.get(sequence);

//            Fill with data
            event.setValue(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);

//            With version 3.0
//            ringBuffer.publishEvent(TRANSLATOR,bb);
        }
    }

    //    With version 3.0 of the Disruptor a richer Lambda-style API was added to help developers by encapsulating this complexity within the Ring Buffer, so post-3.0 the preferred approach for publishing messages is via the Event Publisher/Event Translator portion of the API. E.g.
    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
        @Override
        public void translateTo(LongEvent longEvent, long sequence, ByteBuffer byteBuffer) {
            longEvent.setValue(byteBuffer.getLong(0));
        }
    };
}
