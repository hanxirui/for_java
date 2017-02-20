package com.springboot.zero.dao;

import com.springboot.zero.bean.Demo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by hanxirui on 2017/2/20.
 */
@Repository
public class DemoTemplate {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public Demo getById(long id){
        String sql = "select * from Demo where id=?";
        RowMapper<Demo> rowMapper = new BeanPropertyRowMapper<Demo>(Demo.class);

        return jdbcTemplate.queryForObject(sql,rowMapper,id);
    }
}
