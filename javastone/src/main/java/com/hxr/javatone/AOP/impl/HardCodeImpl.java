package com.hxr.javatone.AOP.impl;

import com.hxr.javatone.AOP.Action;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/14
 * Time: 上午11:11
 */
public class HardCodeImpl implements Action {
    @Override
    public <T> void add(T t) {
        before();
		System.out.println("add " + t + " to somewhere by hardCode");
        after();
    }


    private void before(){
        System.out.println("before");
    }

    private void after(){
        System.out.println("after");
    }

}
