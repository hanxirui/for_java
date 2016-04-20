package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class RoleSch extends BsgridSearch {

    private Integer id;
    private Integer level;
    private String name;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setLevel (Integer level){
        this.level = level;
    }
    
    @ValueField(column = "level")
    public Integer getLevel(){
        return this.level;
    }

    public void setName (String name){
        this.name = name;
    }
    
    @ValueField(column = "name")
    public String getName(){
        return this.name;
    }


}