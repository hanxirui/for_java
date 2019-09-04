package com.hxr.javatone.gc;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/3/5
 * Time: 下午3:01
 */
public class FinalizerTest {

    public static FinalizerTest object;

    public void isAlive(){
        System.out.println("I am alie");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("method finalize is running");
        object = this;
    }

    public static void main(String[] args) throws  Exception{
        object = new FinalizerTest();
        collect();
        collect();
        collect();
    }

	public static void collect() throws Exception{
        object = null;
        System.gc();
        Thread.sleep(500);
		if (object != null) {
            object.isAlive();
		} else {
			System.out.println("I am dead");
		}
	}
}
