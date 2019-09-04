package com.hxr.javatone.threadlocal;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/26
 * Time: 下午3:11
 */
public class ShareValue {

	private static Long shareValue = 10L;

	public Long getValue() {
		return shareValue;
	}

	public void increaseValue(Long value) {
		shareValue = shareValue + value;
	}
}
