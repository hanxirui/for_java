package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class CustomerExtrasSch extends BsgridSearch {

    private Integer id;
    private String idcardnum;
    private String phone;
    private String mobile;
    private String birthday;
    private String carBand;
    private String carNum;
    private String weddingDay;
    private String note;
    private String account;
    private String insertDate;

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

    public void setPhone (String phone){
        this.phone = phone;
    }
    
    @ValueField(column = "phone")
    public String getPhone(){
        return this.phone;
    }

    public void setMobile (String mobile){
        this.mobile = mobile;
    }
    
    @ValueField(column = "mobile")
    public String getMobile(){
        return this.mobile;
    }

    public void setBirthday (String birthday){
        this.birthday = birthday;
    }
    
    @ValueField(column = "birthday")
    public String getBirthday(){
        return this.birthday;
    }

    public void setCarBand (String carBand){
        this.carBand = carBand;
    }
    
    @ValueField(column = "car_band")
    public String getCarBand(){
        return this.carBand;
    }

    public void setCarNum (String carNum){
        this.carNum = carNum;
    }
    
    @ValueField(column = "car_num")
    public String getCarNum(){
        return this.carNum;
    }

    public void setWeddingDay (String weddingDay){
        this.weddingDay = weddingDay;
    }
    
    @ValueField(column = "wedding_day")
    public String getWeddingDay(){
        return this.weddingDay;
    }

    public void setNote (String note){
        this.note = note;
    }
    
    @ValueField(column = "note")
    public String getNote(){
        return this.note;
    }

    public void setAccount (String account){
        this.account = account;
    }
    
    @ValueField(column = "account")
    public String getAccount(){
        return this.account;
    }

    public void setInsertDate (String insertDate){
        this.insertDate = insertDate;
    }
    
    @ValueField(column = "insert_date")
    public String getInsertDate(){
        return this.insertDate;
    }


}