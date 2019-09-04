package com.hxr.javatone.IncreaseLockSample;/** * Created with IntelliJ IDEA. * Project: mobile--web * Author: Kevin * Date: 16/5/22 * Time: 上午10:55 */public class SingleThreadApp {	public static class IncreaseThread<T> extends Thread{		private T increaseObject;		public IncreaseThread(T t) {			this.increaseObject = t;		}		@Override		public void run(){			long timeStart = System.currentTimeMillis();			String threadName = "";			for(long i = 0; i< 1000000000L; i++) {				if(increaseObject instanceof IncreaseObject){					threadName = "Simple";					((IncreaseObject) increaseObject).setSimpleLong(((IncreaseObject) increaseObject).getSimpleLong() + 1);				}else if(increaseObject instanceof SyncIncreaseObject){					threadName = "Sync";					((SyncIncreaseObject) increaseObject)							.setSyncLong(((SyncIncreaseObject) increaseObject).getSyncLong() + 1);				}else if(increaseObject instanceof AtomicIncreaseObject){					threadName = "Atomic";					((AtomicIncreaseObject) increaseObject).getAtomicLong().incrementAndGet();				}			}			long endStart = System.currentTimeMillis();			System.out.println("Single " + threadName + " thread  time consuming " + String.valueOf(endStart - timeStart));		}	}	public static void main(String[] args) {		IncreaseObject increaseObject = new IncreaseObject();		SyncIncreaseObject increaseSyncObject = new SyncIncreaseObject();		AtomicIncreaseObject increaseAtomicObject = new AtomicIncreaseObject();		IncreaseThread increaseThread = new IncreaseThread<IncreaseObject>(increaseObject);		IncreaseThread<SyncIncreaseObject> increaseSyncObjectIncreaseThread = new IncreaseThread<SyncIncreaseObject>(				increaseSyncObject);		IncreaseThread<AtomicIncreaseObject> increaseAtomicObjectIncreaseThread = new IncreaseThread<AtomicIncreaseObject>(				increaseAtomicObject);//		increaseThread.start();//		increaseSyncObjectIncreaseThread.start();//		increaseAtomicObjectIncreaseThread.start();		MultiThreadApp.main(null);	}	private static String single =			  "Single Simple thread  time consuming 5185\n"			+ "Single Atomic thread  time consuming 18208\n"			+ "Single Sync thread  time consuming 39015";	private static String multi =			"Multi Simple thread time consuming4081 final number830355653\n"			+ "Multi Simple thread time consuming4094 final number830595278\n"			+ "Multi Atomic thread time consuming26108 final number995491959\n"			+ "Multi Atomic thread time consuming26249 final number1000000000\n"			+ "Multi Sync thread time consuming43567 final number986536653\n"			+ "Multi Sync thread time consuming43831 final number1000000000";}