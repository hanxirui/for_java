package com.hxr.javatone.threadlocal;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/26
 * Time: 上午11:29
 */
public class LocalValue{

    private static final ThreadLocal<Long> TIME_THREADLOCAL  = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public Long getValue() {
        return TIME_THREADLOCAL.get();
    }
    
    public void increaseValue(Long value){
		TIME_THREADLOCAL.set(TIME_THREADLOCAL.get() + value);
    }

}
