package com.springboot.zero.service;

import com.springboot.zero.bean.Demo;
import com.springboot.zero.dao.DemoRepository;
import com.springboot.zero.dao.DemoTemplate;
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

    @Resource
    private DemoTemplate demoTemplate;

    @Transactional
    public void save(Demo demo){
        demoRepository.save(demo);
    }

    @Transactional
    public void delete(Demo demo){
        demoRepository.delete(demo);
    }

    public Demo get(long id){
        return demoTemplate.getById(id);
    }


}
