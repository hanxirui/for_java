package com.hxr.javatone.funny;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/31
 * Time: 下午7:27
 */
public class helloworld {

    public static void main(String[] args) {
        System.out.println(randomString(-229985452));
        System.out.println(randomString(-147909649));
    }

    public static String randomString(int i) {
        Random ran = new Random(i);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int k = ran.nextInt(27);
            if (k == 0)
                break;

            sb.append((char) ('`' + k));
        }
        return sb.toString();
    }
}
