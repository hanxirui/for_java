package com.springboot.zero.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping(value = { "/", "/index" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String hello(Map<String, Object> map) {
        map.put("hello", "Now is " + getDate());
        return "index";
    }


    private String getDate() {
        Date nowTime = new Date();
        System.out.println(nowTime);// 方法二：Date方式，输出现在时间

        SimpleDateFormat matter = new SimpleDateFormat("现在时间:yyyy年MM月dd日E HH时mm分ss秒");
        System.out.println(matter.format(nowTime));// 方法三：SimpleDateFormat方式，完整输出现在时间

        return matter.format(nowTime);
    }
}
