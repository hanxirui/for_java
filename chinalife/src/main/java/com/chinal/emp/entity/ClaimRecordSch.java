package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class ClaimRecordSch extends BsgridSearch {

    private Integer id;
    private String idcardnum;
    private String reason;
    private String insuranceid;
    private String firstaccount;
    private String firstcontent;
    private String firstclaim;
    private String firsttime;
    private String secondaccount;
    private String secondcontent;
    private String secondtime;
    private String secondclaim;
    private String thirdaccount;
    private String thirdcontent;
    private String thirdclaim;
    private String thirdtime;
    private String fourthaccount;
    private String fourthcontent;
    private String fourthclaim;
    private String fourthtime;
    private String claimtime;
    private String cusname;

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

    public void setReason (String reason){
        this.reason = reason;
    }
    
    @ValueField(column = "reason")
    public String getReason(){
        return this.reason;
    }

    public void setInsuranceid (String insuranceid){
        this.insuranceid = insuranceid;
    }
    
    @ValueField(column = "insuranceid")
    public String getInsuranceid(){
        return this.insuranceid;
    }

    public void setFirstaccount (String firstaccount){
        this.firstaccount = firstaccount;
    }
    
    @ValueField(column = "firstaccount")
    public String getFirstaccount(){
        return this.firstaccount;
    }

    public void setFirstcontent (String firstcontent){
        this.firstcontent = firstcontent;
    }
    
    @ValueField(column = "firstcontent")
    public String getFirstcontent(){
        return this.firstcontent;
    }

    public void setFirstclaim (String firstclaim){
        this.firstclaim = firstclaim;
    }
    
    @ValueField(column = "firstclaim")
    public String getFirstclaim(){
        return this.firstclaim;
    }

    public void setFirsttime (String firsttime){
        this.firsttime = firsttime;
    }
    
    @ValueField(column = "firsttime")
    public String getFirsttime(){
        return this.firsttime;
    }

    public void setSecondaccount (String secondaccount){
        this.secondaccount = secondaccount;
    }
    
    @ValueField(column = "secondaccount")
    public String getSecondaccount(){
        return this.secondaccount;
    }

    public void setSecondcontent (String secondcontent){
        this.secondcontent = secondcontent;
    }
    
    @ValueField(column = "secondcontent")
    public String getSecondcontent(){
        return this.secondcontent;
    }

    public void setSecondtime (String secondtime){
        this.secondtime = secondtime;
    }
    
    @ValueField(column = "secondtime")
    public String getSecondtime(){
        return this.secondtime;
    }

    public void setSecondclaim (String secondclaim){
        this.secondclaim = secondclaim;
    }
    
    @ValueField(column = "secondclaim")
    public String getSecondclaim(){
        return this.secondclaim;
    }

    public void setThirdaccount (String thirdaccount){
        this.thirdaccount = thirdaccount;
    }
    
    @ValueField(column = "thirdaccount")
    public String getThirdaccount(){
        return this.thirdaccount;
    }

    public void setThirdcontent (String thirdcontent){
        this.thirdcontent = thirdcontent;
    }
    
    @ValueField(column = "thirdcontent")
    public String getThirdcontent(){
        return this.thirdcontent;
    }

    public void setThirdclaim (String thirdclaim){
        this.thirdclaim = thirdclaim;
    }
    
    @ValueField(column = "thirdclaim")
    public String getThirdclaim(){
        return this.thirdclaim;
    }

    public void setThirdtime (String thirdtime){
        this.thirdtime = thirdtime;
    }
    
    @ValueField(column = "thirdtime")
    public String getThirdtime(){
        return this.thirdtime;
    }

    public void setFourthaccount (String fourthaccount){
        this.fourthaccount = fourthaccount;
    }
    
    @ValueField(column = "fourthaccount")
    public String getFourthaccount(){
        return this.fourthaccount;
    }

    public void setFourthcontent (String fourthcontent){
        this.fourthcontent = fourthcontent;
    }
    
    @ValueField(column = "fourthcontent")
    public String getFourthcontent(){
        return this.fourthcontent;
    }

    public void setFourthclaim (String fourthclaim){
        this.fourthclaim = fourthclaim;
    }
    
    @ValueField(column = "fourthclaim")
    public String getFourthclaim(){
        return this.fourthclaim;
    }

    public void setFourthtime (String fourthtime){
        this.fourthtime = fourthtime;
    }
    
    @ValueField(column = "fourthtime")
    public String getFourthtime(){
        return this.fourthtime;
    }

    public void setClaimtime (String claimtime){
        this.claimtime = claimtime;
    }
    
    @ValueField(column = "claimtime")
    public String getClaimtime(){
        return this.claimtime;
    }

    public void setCusname (String cusname){
        this.cusname = cusname;
    }
    
    @ValueField(column = "cusname")
    public String getCusname(){
        return this.cusname;
    }


}