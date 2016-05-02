package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class SitRecordSch extends BsgridSearch {

    private Integer id;
    private String account;
    private String visittime;
    private String idcardnum;
    private String content;
    private String name;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setAccount (String account){
        this.account = account;
    }
    
    @ValueField(column = "account")
    public String getAccount(){
        return this.account;
    }

    public void setVisittime (String visittime){
        this.visittime = visittime;
    }
    
    @ValueField(column = "visitTime")
    public String getVisittime(){
        return this.visittime;
    }

    public void setIdcardnum (String idcardnum){
        this.idcardnum = idcardnum;
    }
    
    @ValueField(column = "idcardnum")
    public String getIdcardnum(){
        return this.idcardnum;
    }

    public void setContent (String content){
        this.content = content;
    }
    
    @ValueField(column = "content")
    public String getContent(){
        return this.content;
    }

    public void setName (String name){
        this.name = name;
    }
    
    @ValueField(column = "name")
    public String getName(){
        return this.name;
    }


}