package com.hxr.javatone.collections.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DelayItem<T> implements Delayed {

    /*基于纳秒的时间, 避免包装*/
    private static final long NANO_ORIGN = System.nanoTime();

    /*当前时间与基时间差,以纳秒为单位*/
    final static long now() {
        return System.nanoTime() - NANO_ORIGN;
    }

    /*序列号中断调度关系，进而保证绑定条目中的FIFO顺序。*/
    private static final AtomicLong sequencer = new AtomicLong(0);

    /*sequence number to break ties FIFO*/
    private final long sequenceNumber;

    /*The time the task is enabled to execute in nanoTime units*/
    private final long time;

    private final T item;

    public DelayItem(T submit, long timeout) {
        this.time = now() + timeout;
        this.item = submit;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    public T getItem() {
        return this.item;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);
        return d;
    }

    @Override
    public int compareTo(Delayed other){
        if(other == this){
            return 0;
        }

        if (other instanceof  DelayItem){
            DelayItem x = (DelayItem)other;
            long diff = time - x.time;
            if (diff<0){
                return -1;
            }else if (diff>0){
                return 1;
            }else if(sequenceNumber < x.sequenceNumber){
                return -1;
            }else{
                return 1;
            }

        }

        long d = (getDelay(TimeUnit.NANOSECONDS)-other.getDelay(TimeUnit.NANOSECONDS));
        return (d==0)?0:((d<0)?-1:1);
    }
}
