package com.hxr.javatone.initClass;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/6/17
 * Time: 下午6:32
 */
public class FinalTest {

    public static void main(String[] args){
        System.out.println(Const.NAME);
    }

}

class Const{
    public static final String NAME = "我是常量";
    static{
        System.out.println("初始化Const类");
    }
}
