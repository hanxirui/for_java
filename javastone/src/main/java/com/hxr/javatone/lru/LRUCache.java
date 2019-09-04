package com.hxr.javatone.lru;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/3/16
 * Time: 下午10:07
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private int cacheSize;

    public LRUCache(int cacheSize){
        super(cacheSize,0.75f,true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if(size() > cacheSize){
            System.out.println("remove old");
            return true;
        }else {
            return false;
        }
    }

	@Override
	public V put(K key, V value) {
        System.out.println("data in cache");
		for (Map.Entry<K, V> entry : this.entrySet()) {
			System.out.print(entry.getKey() + " ");
		}
        System.out.println();
        return super.put(key, value);
	}

    public static void main(String[] args) {
        LRUCache<String, Integer> stringIntegerLRUCache = new LRUCache<String, Integer>(5);

        String s = "1，2，3，4，2，1，5，6，2，1，2，3，7，6，3，2，1，2，3，6";
        ArrayList<String> strings = Lists.newArrayList(s.split("，"));
        for (String input: strings){
            stringIntegerLRUCache.put(input, 4);
        }
    }

}
