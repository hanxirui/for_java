package com.hxr.bigdata.redis.raw;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(final String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/data-source.xml");
        RedisClientTemplate redisClient = (RedisClientTemplate) ac.getBean("redisClientTemplate");

        // String
//         redisClient.set("a", "qwe");
//        System.out.println(redisClient.get("a"));

        // 不去重，一直累加
//         redisClient.lpush("message", "hello,my name is zhangsan");
//         redisClient.lpush("message", "my name is lisi");
        // System.out.println(redisClient.lrange("message", 0, -1));
        // System.out.println(redisClient.lpop("message"));
//        System.out.println(redisClient.lrange("message", 0, -1));

        // 去重
//         redisClient.sadd("myset", "first");
//         redisClient.sadd("myset", "second");
//         redisClient.sadd("myset", "third");
//        System.out.println(redisClient.smembers("myset"));

        // 去重
        // redisClient.zadd("hackers", 12, "12");
        // redisClient.zadd("hackers", 16, "16");
        // redisClient.zadd("hackers", 13, "13");
        // redisClient.zadd("hackers", 15, "15");
        // redisClient.zadd("hackers", 12, "12");
        // redisClient.zadd("hackers", 14, "14");
        // System.out.println(redisClient.zrange("hackers", 0, -1));
        // redisClient.zadd("hackers", 12, "21");
//        System.out.println(redisClient.zrange("hackers", 0, -1));

        //
        Map<String, String> pairs = new HashMap<String, String>();
        pairs.put("name", "Bkshi");
        pairs.put("age", "21");
        pairs.put("sex", "Male");
        redisClient.hmset("kid", pairs);
        Map<String, String> pairs1 = new HashMap<String, String>();
        pairs1.put("name", "zhangsan");
        pairs1.put("age", "21");
        pairs1.put("sex", "F");
        redisClient.hmset("kid:001", pairs1);
//        List<String> values = redisClient.hmget("kid", new String[] { "name", "age", "sex" });
//        System.out.println(values);
//        Set<String> setValues = redisClient.hkeys("kid");
//        System.out.println(setValues);
//        values = redisClient.hvals("kid");
//        System.out.println(values);
        pairs = redisClient.hgetAll("kid");
        System.out.println(pairs);
        pairs1 = redisClient.hgetAll("kid:001");
        System.out.println(pairs1);
    }
}
