package com.hxr.javatone.initClass;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/6/17
 * Time: 下午6:07
 */
public class Father extends Super {
    public static int m = 33;
    static{
        System.out.println("父类被初始化");
    }
}
