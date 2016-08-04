package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class FenpeilishiSch extends BsgridSearch {

    private String fenpeirenCode;
    private String fenpeirenName;
    private String kehujingliCode;
    private String kehujingliName;
    private String customerId;
    private String customerName;
    private String fenpeishijian;

    public void setFenpeirenCode (String fenpeirenCode){
        this.fenpeirenCode = fenpeirenCode;
    }
    
    @ValueField(column = "fenpeiren_code")
    public String getFenpeirenCode(){
        return this.fenpeirenCode;
    }

    public void setFenpeirenName (String fenpeirenName){
        this.fenpeirenName = fenpeirenName;
    }
    
    @ValueField(column = "fenpeiren_name")
    public String getFenpeirenName(){
        return this.fenpeirenName;
    }

    public void setKehujingliCode (String kehujingliCode){
        this.kehujingliCode = kehujingliCode;
    }
    
    @ValueField(column = "kehujingli_code")
    public String getKehujingliCode(){
        return this.kehujingliCode;
    }

    public void setKehujingliName (String kehujingliName){
        this.kehujingliName = kehujingliName;
    }
    
    @ValueField(column = "kehujingli_name")
    public String getKehujingliName(){
        return this.kehujingliName;
    }

    public void setCustomerId (String customerId){
        this.customerId = customerId;
    }
    
    @ValueField(column = "customer_id")
    public String getCustomerId(){
        return this.customerId;
    }

    public void setCustomerName (String customerName){
        this.customerName = customerName;
    }
    
    @ValueField(column = "customer_name")
    public String getCustomerName(){
        return this.customerName;
    }

    public void setFenpeishijian (String fenpeishijian){
        this.fenpeishijian = fenpeishijian;
    }
    
    @ValueField(column = "fenpeishijian")
    public String getFenpeishijian(){
        return this.fenpeishijian;
    }


}