package com.hxr.bigdata.redis.springdata;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hxr.bigdata.redis.springdata.dao.UserDao;
import com.hxr.bigdata.redis.springdata.vo.UserVo;

public class App {
    public static void main(final String[] args) {
        ApplicationContext ac =  new ClassPathXmlApplicationContext(new String[] {"classpath:/applicationContext.xml", "classpath:/data-source.xml"});
        UserDao userDAO = (UserDao)ac.getBean("userDAO");
        UserVo user1 = new UserVo();
        user1.setId(1);
        user1.setName("obama");
        userDAO.saveUserVo(user1);
        UserVo user2 = userDAO.getUserVo(1);
        System.out.println(user2.getName());
    }
}
