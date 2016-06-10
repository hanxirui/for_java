package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class BankRecordSch extends BsgridSearch {

    private Integer id;
    private String bankname;
    private String bankcode;
    private String zhihangname;
    private String zhihangcode;
    private String wangdianname;
    private String wangdiancode;
    private String mzhuanguanyuan;
    private String mzhuanguanyuancode;
    private String szhuanguanyuan;
    private String szhuanguanyuancode;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setBankname (String bankname){
        this.bankname = bankname;
    }
    
    @ValueField(column = "bankname")
    public String getBankname(){
        return this.bankname;
    }

    public void setBankcode (String bankcode){
        this.bankcode = bankcode;
    }
    
    @ValueField(column = "bankcode")
    public String getBankcode(){
        return this.bankcode;
    }

    public void setZhihangname (String zhihangname){
        this.zhihangname = zhihangname;
    }
    
    @ValueField(column = "zhihangname")
    public String getZhihangname(){
        return this.zhihangname;
    }

    public void setZhihangcode (String zhihangcode){
        this.zhihangcode = zhihangcode;
    }
    
    @ValueField(column = "zhihangcode")
    public String getZhihangcode(){
        return this.zhihangcode;
    }

    public void setWangdianname (String wangdianname){
        this.wangdianname = wangdianname;
    }
    
    @ValueField(column = "wangdianname")
    public String getWangdianname(){
        return this.wangdianname;
    }

    public void setWangdiancode (String wangdiancode){
        this.wangdiancode = wangdiancode;
    }
    
    @ValueField(column = "wangdiancode")
    public String getWangdiancode(){
        return this.wangdiancode;
    }

    public void setMzhuanguanyuan (String mzhuanguanyuan){
        this.mzhuanguanyuan = mzhuanguanyuan;
    }
    
    @ValueField(column = "mzhuanguanyuan")
    public String getMzhuanguanyuan(){
        return this.mzhuanguanyuan;
    }

    public void setMzhuanguanyuancode (String mzhuanguanyuancode){
        this.mzhuanguanyuancode = mzhuanguanyuancode;
    }
    
    @ValueField(column = "mzhuanguanyuancode")
    public String getMzhuanguanyuancode(){
        return this.mzhuanguanyuancode;
    }

    public void setSzhuanguanyuan (String szhuanguanyuan){
        this.szhuanguanyuan = szhuanguanyuan;
    }
    
    @ValueField(column = "szhuanguanyuan")
    public String getSzhuanguanyuan(){
        return this.szhuanguanyuan;
    }

    public void setSzhuanguanyuancode (String szhuanguanyuancode){
        this.szhuanguanyuancode = szhuanguanyuancode;
    }
    
    @ValueField(column = "szhuanguanyuancode")
    public String getSzhuanguanyuancode(){
        return this.szhuanguanyuancode;
    }


}