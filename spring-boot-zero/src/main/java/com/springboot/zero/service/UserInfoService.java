package com.springboot.zero.service;

import com.springboot.zero.dao.UserInfoRepository;
import com.springboot.zero.security.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hanxirui on 2017/2/23.
 */
@Service
public class UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;

    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userInfoRepository.findByUsername(username);
    }

}
