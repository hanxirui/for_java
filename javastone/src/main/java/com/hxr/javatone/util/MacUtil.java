package com.hxr.javatone.util;

import java.util.Locale;

public class MacUtil {
    /**
     * Mac尾数加法
     * @author YOLANDA
     * @param mac Mac地址：CAFDEFB469DF
     * @param add 要加的数
     * @return 返回为数加 减后的Mac地址
     */
    public static String getMacAdd(String mac, final int add){
        String lastChar = mac.substring(mac.length() - 1).toUpperCase(Locale.getDefault());
        mac = mac.substring(0, mac.length() - 1);
        if("F".equals(lastChar)){
            lastChar = "0";
        } else {
            int tempChar = Integer.parseInt(lastChar, 16) + add;
            lastChar = Integer.toHexString(tempChar).toUpperCase(Locale.getDefault());
        }
        return (mac + lastChar);
    }
    
    /**
     * Mac尾数减法
     * @author YOLANDA
     * @param mac Mac地址：CAFDEFB469DF
     * @param minus 要减的数
     * @return 返回为数加 减后的Mac地址
     */
    public static String getMacMinus(String mac, final int minus){
        String lastChar = mac.substring(mac.length() - 1).toUpperCase(Locale.getDefault());
        mac = mac.substring(0, mac.length() - 1);
        if("0".equals(lastChar)){
            lastChar = "F";
        } else {
            int tempChar = Integer.parseInt(lastChar, 16) - minus;
            lastChar = Integer.toHexString(tempChar).toUpperCase(Locale.getDefault());
        }
        return (mac + lastChar);
    }
}
