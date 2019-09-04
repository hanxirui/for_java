package com.hxr.javatone.AOP.impl;

import com.hxr.javatone.AOP.Action;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/14
 * Time: 上午10:32
 */
public class DateSourceImpl implements Action {

    @Override
    public <T> void add(T t) {
		System.out.println("add " + t + " to db");
    }

}
