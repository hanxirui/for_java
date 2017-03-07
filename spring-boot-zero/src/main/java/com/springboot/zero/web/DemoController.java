package com.springboot.zero.web;

import com.springboot.zero.environment.CustomizedProperties;
import com.springboot.zero.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.springboot.zero.bean.Demo;

import javax.annotation.Resource;

/**
 * 测试.
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/demo")
public class DemoController {


    @Resource
    private DemoService demoService;

    @RequestMapping("/save")
    public String save(String name) {
        Demo demo = new Demo();
        demo.setName(name);

        demoService.save(demo);
        return "success";
    }

    /**
     * 返回demo数据:
     * 请求地址：http://127.0.0.1:8080/demo/getDemo
     *
     * @return
     */
    @RequestMapping("/get")
    public Demo getDemo(long id) {

        return demoService.get(id);
    }

    //地址：http://127.0.0.1:8080/demo/getFastJson
    @RequestMapping("/getFastJson")
    public String getFastJson() {
        Demo demo = new Demo();
        demo.setId(2);
        demo.setName("Angel2");
        return JSONObject.toJSONString(demo);
    }

    @RequestMapping("/zeroException")
    public int zeroException() {
        return 100 / 0;
    }

}
