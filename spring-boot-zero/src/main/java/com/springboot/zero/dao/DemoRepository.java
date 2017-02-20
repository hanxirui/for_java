package com.springboot.zero.dao;

import com.springboot.zero.bean.Demo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by hanxirui on 2017/2/20.
 */
public interface DemoRepository extends CrudRepository<Demo, Long> {
}
