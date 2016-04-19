package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class EmployeeSch extends BsgridSearch {

    private Integer id;
    private String name;
    private Integer role;
    private String password;

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

    public void setRole (Integer role){
        this.role = role;
    }
    
    @ValueField(column = "role")
    public Integer getRole(){
        return this.role;
    }

    public void setPassword (String password){
        this.password = password;
    }
    
    @ValueField(column = "password")
    public String getPassword(){
        return this.password;
    }


}