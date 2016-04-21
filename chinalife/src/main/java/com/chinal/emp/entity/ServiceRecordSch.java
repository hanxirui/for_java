package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class ServiceRecordSch extends BsgridSearch {

    private Integer id;
    private String idcardnum;
    private String servicetime;
    private String content;
    private String account;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setIdcardnum (String idcardnum){
        this.idcardnum = idcardnum;
    }
    
    @ValueField(column = "idcardnum")
    public String getIdcardnum(){
        return this.idcardnum;
    }

    public void setServicetime (String servicetime){
        this.servicetime = servicetime;
    }
    
    @ValueField(column = "servicetime")
    public String getServicetime(){
        return this.servicetime;
    }

    public void setContent (String content){
        this.content = content;
    }
    
    @ValueField(column = "content")
    public String getContent(){
        return this.content;
    }

    public void setAccount (String account){
        this.account = account;
    }
    
    @ValueField(column = "account")
    public String getAccount(){
        return this.account;
    }


}