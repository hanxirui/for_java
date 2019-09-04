package com.hxr.javatone.ClassObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/9/9
 * Time: 下午8:49
 */
public class CatTest {

	public static void main(String[] args) {
		Class c = Cat.class;
		Field[] declaredFields = c.getDeclaredFields();
		for (Field field : declaredFields) {
			System.out.println(field);
		}


        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods){
            System.out.println(method);
        }


        try {
            Cat cat = (Cat) c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Class catClass = Class.forName("ClassObject.Cat");
            Field name = catClass.getDeclaredField("name");
            //设置为可以访问private属性g
            name.setAccessible(true);
            Cat cat2 = (Cat) catClass.newInstance();
            name.set(cat2,"Aristark");
            System.out.println(cat2.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
	}


}
