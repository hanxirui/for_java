package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class InsuranceRecordSch extends BsgridSearch {

    private Integer id;
    private String orgCode;
    private String insuranceNum;
    private String touInsuranceNum;
    private String typeCode;
    private String qudao;
    private String insuranceFei;
    private String totalInsuranceFei;
    private String manqiDate;
    private String feiType;
    private String feiQijian;
    private String insuranceQijian;
    private String state;
    private String customerIdcardnum;
    private String customerAddr;
    private String customerPhone;
    private String customerMobile;
    private String account;
    private String accountCode;
    private String bankName;
    private String bankCardNum;
    private String beibaoxianren;
    private String shouyiren;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setOrgCode (String orgCode){
        this.orgCode = orgCode;
    }
    
    @ValueField(column = "org_code")
    public String getOrgCode(){
        return this.orgCode;
    }

    public void setInsuranceNum (String insuranceNum){
        this.insuranceNum = insuranceNum;
    }
    
    @ValueField(column = "insurance_num")
    public String getInsuranceNum(){
        return this.insuranceNum;
    }

    public void setTouInsuranceNum (String touInsuranceNum){
        this.touInsuranceNum = touInsuranceNum;
    }
    
    @ValueField(column = "tou_insurance_num")
    public String getTouInsuranceNum(){
        return this.touInsuranceNum;
    }

    public void setTypeCode (String typeCode){
        this.typeCode = typeCode;
    }
    
    @ValueField(column = "type_code")
    public String getTypeCode(){
        return this.typeCode;
    }

    public void setQudao (String qudao){
        this.qudao = qudao;
    }
    
    @ValueField(column = "qudao")
    public String getQudao(){
        return this.qudao;
    }

    public void setInsuranceFei (String insuranceFei){
        this.insuranceFei = insuranceFei;
    }
    
    @ValueField(column = "insurance_fei")
    public String getInsuranceFei(){
        return this.insuranceFei;
    }

    public void setTotalInsuranceFei (String totalInsuranceFei){
        this.totalInsuranceFei = totalInsuranceFei;
    }
    
    @ValueField(column = "total_insurance_fei")
    public String getTotalInsuranceFei(){
        return this.totalInsuranceFei;
    }

    public void setManqiDate (String manqiDate){
        this.manqiDate = manqiDate;
    }
    
    @ValueField(column = "manqi_date")
    public String getManqiDate(){
        return this.manqiDate;
    }

    public void setFeiType (String feiType){
        this.feiType = feiType;
    }
    
    @ValueField(column = "fei_type")
    public String getFeiType(){
        return this.feiType;
    }

    public void setFeiQijian (String feiQijian){
        this.feiQijian = feiQijian;
    }
    
    @ValueField(column = "fei_qijian")
    public String getFeiQijian(){
        return this.feiQijian;
    }

    public void setInsuranceQijian (String insuranceQijian){
        this.insuranceQijian = insuranceQijian;
    }
    
    @ValueField(column = "insurance_qijian")
    public String getInsuranceQijian(){
        return this.insuranceQijian;
    }

    public void setState (String state){
        this.state = state;
    }
    
    @ValueField(column = "state")
    public String getState(){
        return this.state;
    }

    public void setCustomerIdcardnum (String customerIdcardnum){
        this.customerIdcardnum = customerIdcardnum;
    }
    
    @ValueField(column = "customer_idcardnum")
    public String getCustomerIdcardnum(){
        return this.customerIdcardnum;
    }

    public void setCustomerAddr (String customerAddr){
        this.customerAddr = customerAddr;
    }
    
    @ValueField(column = "customer_addr")
    public String getCustomerAddr(){
        return this.customerAddr;
    }

    public void setCustomerPhone (String customerPhone){
        this.customerPhone = customerPhone;
    }
    
    @ValueField(column = "customer_phone")
    public String getCustomerPhone(){
        return this.customerPhone;
    }

    public void setCustomerMobile (String customerMobile){
        this.customerMobile = customerMobile;
    }
    
    @ValueField(column = "customer_mobile")
    public String getCustomerMobile(){
        return this.customerMobile;
    }

    public void setAccount (String account){
        this.account = account;
    }
    
    @ValueField(column = "account")
    public String getAccount(){
        return this.account;
    }

    public void setAccountCode (String accountCode){
        this.accountCode = accountCode;
    }
    
    @ValueField(column = "account_code")
    public String getAccountCode(){
        return this.accountCode;
    }

    public void setBankName (String bankName){
        this.bankName = bankName;
    }
    
    @ValueField(column = "bank_name")
    public String getBankName(){
        return this.bankName;
    }

    public void setBankCardNum (String bankCardNum){
        this.bankCardNum = bankCardNum;
    }
    
    @ValueField(column = "bank_card_num")
    public String getBankCardNum(){
        return this.bankCardNum;
    }

    public void setBeibaoxianren (String beibaoxianren){
        this.beibaoxianren = beibaoxianren;
    }
    
    @ValueField(column = "beibaoxianren")
    public String getBeibaoxianren(){
        return this.beibaoxianren;
    }

    public void setShouyiren (String shouyiren){
        this.shouyiren = shouyiren;
    }
    
    @ValueField(column = "shouyiren")
    public String getShouyiren(){
        return this.shouyiren;
    }


}