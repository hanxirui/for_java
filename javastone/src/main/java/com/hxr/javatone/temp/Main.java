package com.hxr.javatone.temp;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanxirui on 2017/6/29.
 */
public class Main {
    public static void main(String[] args) {
            Department deta = new Department();
        deta.setId("deta");
        deta.setName("德塔");
            Department yingyong = new Department();
        yingyong.setId("yingyong");
        yingyong.setName("应用开发部");
            Department pingtai = new Department();
        pingtai.setId("pingtai");
        pingtai.setName("平台开发部");
            Department qianduan = new Department();
        qianduan.setId("qianduan");
        qianduan.setName("前端开发部");
            Department app = new Department();
        app.setId("app");
        app.setName("app开发部");
            Department beijing = new Department();
        beijing.setId("beijing");
        beijing.setName("天津开发部");
            Department tianjin = new Department();
        tianjin.setId("tianjin");
        tianjin.setName("天津开发部");
            Department shenyang = new Department();
        shenyang.setId("shengyang");
        shenyang.setName("沈阳开发部");
            List<Department> pingtais = new ArrayList<Department>();
        pingtais.add(qianduan);
        pingtais.add(app);
        yingyong.setChildren(pingtais);
            List<Department> tianjins = new ArrayList<Department>();
        tianjins.add(yingyong);
        tianjins.add(pingtai);
        tianjin.setChildren(tianjins);
            List<Department> detas = new ArrayList<Department>();
        detas.add(tianjin);
        detas.add(beijing);
        detas.add(shenyang);
        deta.setChildren(detas);

        System.out.println(JSONObject.fromObject(deta).toString());
    }
}
