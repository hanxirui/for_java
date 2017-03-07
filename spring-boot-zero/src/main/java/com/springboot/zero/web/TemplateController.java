package com.springboot.zero.web;

/**
 * Created by hanxirui on 2017/2/21.
 */

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模板测试.
 *
 * @author Administrator
 */
@Controller
public class TemplateController {

    // 从 application.properties 中读取配置，如取不到默认值为Hello Shanhy
    @Value("${application.hello:Hello Angel}")
    private String hello;

    /**
     * 返回Thymeleaf模板.
     */
    @RequestMapping("/thymeleafhello")
    public String helloHtml(Map<String, Object> map) {
        map.put("hello", hello);
        return "/helloThymeleaf";
    }

    /**
     * 返回html模板.
     */
    @RequestMapping("/ftlhello")
    public String helloFtl(Map<String,Object> map){
        map.put("hello",hello);
        return "helloFtl";
    }



}

