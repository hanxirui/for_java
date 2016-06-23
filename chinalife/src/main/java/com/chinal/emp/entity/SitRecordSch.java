package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class SitRecordSch extends BsgridSearch {

    private Integer id;
    private String empcode;
    private String visittime;
    private String idcardnum;
    private String content;
    private String name;
    private String type;
    private String xijie;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setEmpcode (String empcode){
        this.empcode = empcode;
    }
    
    @ValueField(column = "empcode")
    public String getEmpcode(){
        return this.empcode;
    }

    public void setVisittime (String visittime){
        this.visittime = visittime;
    }
    
    @ValueField(column = "visittime")
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

    public void setType (String type){
        this.type = type;
    }
    
    @ValueField(column = "type")
    public String getType(){
        return this.type;
    }

    public void setXijie (String xijie){
        this.xijie = xijie;
    }
    
    @ValueField(column = "xijie")
    public String getXijie(){
        return this.xijie;
    }


}