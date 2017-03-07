package com.springboot.zero.service;

import com.springboot.zero.bean.Demo;
import com.springboot.zero.dao.DemoRepository;
import com.springboot.zero.dao.DemoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by hanxirui on 2017/2/20.
 */
@Service
public class DemoService {
    @Resource
    private DemoRepository demoRepository;

    @Autowired
    private DemoTemplate demoTemplate;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Transactional
    public void save(Demo demo) {
        demoRepository.save(demo);
    }

    @Transactional
    public void delete(Demo demo) {
        demoRepository.delete(demo);
    }

    @CacheEvict(value="demo")
    public void deleteFromCache(long id) {
        System.out.println("从缓存中删除--------------------------"+id);
    }

    @Cacheable(value="demo")
    public Demo get(long id) {
        System.err.println("从数据库中进行获取的--------------------------id="+id);
        return demoTemplate.getById(id);
    }

}
