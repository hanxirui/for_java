package com.hxr.javatone.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2016/12/18
 * Time: 下午4:23
 */
public class LoadSource {


    public LoadSource() {
    }

    Properties loadUseClassLoader(){
        Properties properties = new Properties();
        InputStream resourceAsStream = LoadSource.class.getClassLoader().getResourceAsStream("test/source.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    Properties loadUseClass(){
        Properties properties = new Properties();
        InputStream resourceAsStream = LoadSource.class.getResourceAsStream("/test/source.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static void main(String[] args) {
        LoadSource loadSource = new LoadSource();
        System.out.println(loadSource.loadUseClass().get("key"));
        System.out.println(loadSource.loadUseClassLoader().get("key"));
    }
}
