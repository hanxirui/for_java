package com.hxr.javatone.AOP.impl;

import com.hxr.javatone.AOP.Action;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/14
 * Time: 上午11:07
 */
public class CacheImpl implements Action {
    
    @Override
    public <T> void add(T t) {
		System.out.println("add " + t + " to cache");
    }
}
