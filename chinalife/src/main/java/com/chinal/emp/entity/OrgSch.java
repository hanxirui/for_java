package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class OrgSch extends BsgridSearch {

    private Integer id;
    private String name;
    private String code;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setName (String name){
        this.name = name;
    }
    
    @ValueField(column = "name")
    public String getName(){
        return this.name;
    }

    public void setCode (String code){
        this.code = code;
    }
    
    @ValueField(column = "code")
    public String getCode(){
        return this.code;
    }


}