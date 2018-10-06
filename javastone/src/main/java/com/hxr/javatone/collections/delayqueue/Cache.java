package com.hxr.javatone.collections.delayqueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cache<K, V> {
    private static final Logger log = Logger.getLogger(Cache.class.getName());
    private ConcurrentMap<K, DelayItem> cacheObjMap = new ConcurrentHashMap<K, DelayItem>();

    private DelayQueue<DelayItem<Pair<K, V>>> queue = new DelayQueue<>();

    private Thread daemonThread;

    public Cache() {
        Runnable daemonTask = new Runnable() {
            @Override
            public void run() {
                daemonCheck();
            }
        };

        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Cache Daemon");
        daemonThread.start();
    }

    private void daemonCheck() {
        log.info("cache service started.");

        for (; ; ) {
            try {
                DelayItem<Pair<K, V>> item = queue.take();
                log.info("------"+System.currentTimeMillis());
                if (item != null) {
                    log.info("------"+System.currentTimeMillis());
//                    超时对象处理
                    Pair<K, V> pair = item.getItem();
                    cacheObjMap.remove(pair.key, pair.value);
                }
            } catch (InterruptedException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
                break;
            }
        }

        log.info("cache service stopped");
    }

    public void put(K key, V value, long time, TimeUnit unit){
        long nanoTime = TimeUnit.NANOSECONDS.convert(time,unit);
        DelayItem item = new DelayItem<Pair<K, V>>(new Pair<K, V>(key,value),nanoTime);

        DelayItem oldValue = cacheObjMap.put(key,item);
        if (oldValue != null){
            queue.remove(oldValue);
        }

        queue.put(item);
    }

    public DelayItem get(K key){
        return cacheObjMap.get(key);
    }

    // 测试入口函数
    public static void main(String[] args) throws Exception {
        Cache<Integer, String> cache = new Cache<Integer, String>();
        cache.put(1, "aaaa", 3, TimeUnit.SECONDS);
        cache.put(1, "aaaa", 3, TimeUnit.SECONDS);

//        Thread.sleep(1000 * 2);
        {
            DelayItem str = cache.get(1);
//            System.out.println(str);
        }

//        Thread.sleep(1000 * 2);

        while (true){
            DelayItem str = cache.get(1);
//            System.out.println(str);
            if(str==null){
                break;
            }
        }
    }
}
