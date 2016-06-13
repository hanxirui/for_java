package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class BizplatformSch extends BsgridSearch {

    private Integer id;
    private String title;
    private String zhishibaifang;
    private String caiye;
    private String startdate;
    private String enddate;
    private String huashu;
    private String jishuziliao;
    private String others;
    private String empcode;
    private String orgcode;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setTitle (String title){
        this.title = title;
    }
    
    @ValueField(column = "title")
    public String getTitle(){
        return this.title;
    }

    public void setZhishibaifang (String zhishibaifang){
        this.zhishibaifang = zhishibaifang;
    }
    
    @ValueField(column = "zhishibaifang")
    public String getZhishibaifang(){
        return this.zhishibaifang;
    }

    public void setCaiye (String caiye){
        this.caiye = caiye;
    }
    
    @ValueField(column = "caiye")
    public String getCaiye(){
        return this.caiye;
    }

    public void setStartdate (String startdate){
        this.startdate = startdate;
    }
    
    @ValueField(column = "startdate")
    public String getStartdate(){
        return this.startdate;
    }

    public void setEnddate (String enddate){
        this.enddate = enddate;
    }
    
    @ValueField(column = "enddate")
    public String getEnddate(){
        return this.enddate;
    }

    public void setHuashu (String huashu){
        this.huashu = huashu;
    }
    
    @ValueField(column = "huashu")
    public String getHuashu(){
        return this.huashu;
    }

    public void setJishuziliao (String jishuziliao){
        this.jishuziliao = jishuziliao;
    }
    
    @ValueField(column = "jishuziliao")
    public String getJishuziliao(){
        return this.jishuziliao;
    }

    public void setOthers (String others){
        this.others = others;
    }
    
    @ValueField(column = "others")
    public String getOthers(){
        return this.others;
    }

    public void setEmpcode (String empcode){
        this.empcode = empcode;
    }
    
    @ValueField(column = "empcode")
    public String getEmpcode(){
        return this.empcode;
    }

    public void setOrgcode (String orgcode){
        this.orgcode = orgcode;
    }
    
    @ValueField(column = "orgcode")
    public String getOrgcode(){
        return this.orgcode;
    }


}