package com.hxr.javatone.AOP.impl;

import com.hxr.javatone.AOP.Action;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/15
 * Time: 下午2:40
 */
@Component
public class PureImpl implements Action {


	@Override
	public <T> void add(T t) {
		System.out.println("add " + t + " to somewhere by pureImpl");
	}
}
