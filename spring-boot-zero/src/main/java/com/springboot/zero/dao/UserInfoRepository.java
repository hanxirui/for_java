package com.springboot.zero.dao;

import com.springboot.zero.security.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by hanxirui on 2017/2/23.
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    /** 通过username查找用户信息; */
    public UserInfo findByUsername(String username);

}