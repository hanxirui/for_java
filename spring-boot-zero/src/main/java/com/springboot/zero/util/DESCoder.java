package com.springboot.zero.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @Description: DES对称加密算法 
 * @Copyright: 北京德塔精要信息技术有限公司 (c)2016
 * @Created Date : 2016年7月18日
 * @author HeHangjie
 * @vesion 1.0
 */
public class DESCoder {  
    /** 
     * 密钥算法 
     * java支持56位密钥，bouncycastle支持64位 
     * */  
    public static final String KEY_ALGORITHM="DES";  
      
    /** 
     * 加密/解密算法/工作模式/填充方式 
     * */  
    public static final String CIPHER_ALGORITHM="DES/ECB/PKCS5Padding";  
      
    /** 
     *  
     * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥 
     * @return byte[] 二进制密钥 
     * */  
    public static byte[] initkey() throws Exception{  
          
        //实例化密钥生成器  
        KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);  
        //初始化密钥生成器  
        kg.init(56);  
        //生成密钥  
        SecretKey secretKey=kg.generateKey();  
        //获取二进制密钥编码形式  
        return secretKey.getEncoded();  
    }  
    /** 
     * 转换密钥 
     * @param key 二进制密钥 
     * @return Key 密钥 
     * */  
    public static Key toKey(byte[] key) throws Exception{  
        //实例化Des密钥  
        DESKeySpec dks=new DESKeySpec(key);  
        //实例化密钥工厂  
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        //生成密钥  
        SecretKey secretKey=keyFactory.generateSecret(dks);  
        return secretKey;  
    }  
      
    /** 
     * 加密数据 
     * @param data 待加密数据 
     * @param key 密钥 
     * @return byte[] 加密后的数据 
     * */  
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{  
        //还原密钥  
        Key k=toKey(key);  
        //实例化  
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);  
        //初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, k);  
        //执行操作  
        return cipher.doFinal(data);  
    }  
    /** 
     * 解密数据 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密后的数据 
     * */  
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{  
        //欢迎密钥  
        Key k =toKey(key);  
        //实例化  
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);  
        //初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, k);  
        //执行操作  
        return cipher.doFinal(data);  
    }

    public static String getMD5(String input) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /** 
     * @param args 
     * @throws Exception  
     */  
//    public static void main(String[] args) throws Exception {
//        String str="Hello, Link API 0.1.1";
//        System.out.println("原文："+str);
//        //初始化密钥
//        //byte[] key=DESCoder.initkey();
//        byte[] key = "74cfa00991319842".getBytes();
//        System.out.println("密钥："+Base64.encodeBase64String(key));
//        //加密数据
//        byte[] data=DESCoder.encrypt(str.getBytes(), key);
//        System.out.println("加密后："+Base64.encodeBase64String(data));
//
//        //解密数据
///*        data=DESCoder.decrypt((new String(data)).getBytes(), key);
//        System.out.println("解密后："+new String(data));*/
//
//
//        String ip = "123.123.123.123";
//
//        String orig = str + "1487250214" + ip;
//
//        System.out.println(getMD5(orig));
//        System.out.println(getMD5(orig));
//        System.out.println(getMD5(orig));
//        System.out.println(getMD5(orig));
//    }
}  
