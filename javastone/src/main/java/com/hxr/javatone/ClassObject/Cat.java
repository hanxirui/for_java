package com.hxr.javatone.ClassObject;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/9/9
 * Time: 下午8:47
 */
public class Cat {

    public static int count;
    public int age;
    private String name;

    static {
        count = 0;
    }

    public Cat(){
        age = count++;
        System.out.println("this is class Cat!");

    }

    public void run(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
