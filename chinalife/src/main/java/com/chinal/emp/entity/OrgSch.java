package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class OrgSch extends BsgridSearch {

    private Integer id;
    private String name;
    private String type;

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

    public void setType (String type){
        this.type = type;
    }
    
    @ValueField(column = "type")
    public String getType(){
        return this.type;
    }


}