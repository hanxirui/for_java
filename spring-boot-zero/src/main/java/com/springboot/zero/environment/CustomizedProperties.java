package com.springboot.zero.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hanxirui on 2017/2/22.
 */
@ConfigurationProperties(prefix = "customized")
public class CustomizedProperties {
  String name;
  String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
