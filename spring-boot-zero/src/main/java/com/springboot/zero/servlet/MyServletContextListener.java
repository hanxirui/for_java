package com.springboot.zero.servlet;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 使用@WebListener注解，实现ServletContextListener接口
 *
 * @author Angel(QQ:412887952)
 * @version v.0.1
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Listener>>>>>>>>>>>>>>>ServletContex销毁");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Listener>>>>>>>>>>>>>>>ServletContex初始化");
    }
}
