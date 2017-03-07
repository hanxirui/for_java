package com.springboot.zero.web;

import com.springboot.zero.environment.CustomizedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 标记为：restful
public class HelloController{


        @Autowired
        private CustomizedProperties properties;

        @RequestMapping("/hello")
        public String hello() {
            return "Hello "+properties.getName();
        }
}
