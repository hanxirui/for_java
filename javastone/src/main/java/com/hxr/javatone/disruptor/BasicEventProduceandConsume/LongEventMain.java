package com.hxr.javatone.disruptor.BasicEventProduceandConsume;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LongEventMain {
//    public static void main(String[] args) throws InterruptedException {
////        ThreadFactory that will be used to construct new threads for consumers
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//
////        The factory for the event
//        LongEventFactory factory = new LongEventFactory();
//
////        specify the size of the ring buffer,must be power of 2
//        int bufferSize = 1024;
//
//        //Construct the Disruptor
//        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, threadFactory);
//
////        Connect the handler
//        disruptor.handleEventsWith(new LongEventHandler());
//
////        Start the Disruptor, starts all threads running
//        disruptor.start();
//
////        Get the ring buffer from the Disruptor to be used for publishing.
//        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
//
//        LongEventProducer producer = new LongEventProducer(ringBuffer);
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
//
//        for (int i = 0; i < 100; i++) {
//            byteBuffer.putLong(0, i);
//            producer.onData(byteBuffer);
//            Thread.sleep(1000);
//        }
//    }

    /**
     * java8 version
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
//        ThreadFactory that will be used to construct new threads for consumers
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

//        specify the size of the ring buffer,must be power of 2
        int bufferSize = 1024;

        //Construct the Disruptor
//        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, threadFactory);
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

//        Connect the handler
        disruptor.handleEventsWith((longEvent, sequence, endOfBatch) -> System.out.println(sequence+" :: "+"Event: "+longEvent.getValue()+" endOfBatch: "+endOfBatch));

//        Start the Disruptor, starts all threads running
        disruptor.start();

//        Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (int i = 0; i < 100; i++) {
            byteBuffer.putLong(0, i);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.setValue(buffer.getLong(0)), byteBuffer);
            Thread.sleep(1000);
        }
    }
}
