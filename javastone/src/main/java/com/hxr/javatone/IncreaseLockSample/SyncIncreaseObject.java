package com.hxr.javatone.IncreaseLockSample;/** * Created with IntelliJ IDEA. * Project: test-jar * Author: Kevin * Date: 16/5/22 * Time: 上午10:53 */public class SyncIncreaseObject {	private long syncLong;	public synchronized long getSyncLong() {		return syncLong;	}	public synchronized void setSyncLong(long syncLong) {		this.syncLong = syncLong;	}	public synchronized void increase(){		syncLong = syncLong + 1;	}}