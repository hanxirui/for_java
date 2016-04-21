package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class CustomerBasicSch extends BsgridSearch {

    private Integer id;
    private String name;
    private String idcardnum;
    private Integer type;

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

    public void setIdcardnum (String idcardnum){
        this.idcardnum = idcardnum;
    }
    
    @ValueField(column = "idcardnum")
    public String getIdcardnum(){
        return this.idcardnum;
    }

    public void setType (Integer type){
        this.type = type;
    }
    
    @ValueField(column = "type")
    public Integer getType(){
        return this.type;
    }


}