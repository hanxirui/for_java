package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.annotation.ValueField;

import org.durcframework.core.support.BsgridSearch;

public class BizRecordSch extends BsgridSearch {

    private Integer id;
    private String yaoyueNum;
    private String daohuiNum;
    private String receiveNum;
    private String receiveBaofei;
    private String daohuilv;
    private String qiandanNum;
    private String qiandanlv;
    private String huishoulv;
    private String kehujingli;
    private String qiandanBaofei;
    private String riqi;
    private String bizplatTitle;
    private Integer bizpaltId;

    public void setId (Integer id){
        this.id = id;
    }
    
    @ValueField(column = "id")
    public Integer getId(){
        return this.id;
    }

    public void setYaoyueNum (String yaoyueNum){
        this.yaoyueNum = yaoyueNum;
    }
    
    @ValueField(column = "yaoyue_num")
    public String getYaoyueNum(){
        return this.yaoyueNum;
    }

    public void setDaohuiNum (String daohuiNum){
        this.daohuiNum = daohuiNum;
    }
    
    @ValueField(column = "daohui_num")
    public String getDaohuiNum(){
        return this.daohuiNum;
    }

    public void setReceiveNum (String receiveNum){
        this.receiveNum = receiveNum;
    }
    
    @ValueField(column = "receive_num")
    public String getReceiveNum(){
        return this.receiveNum;
    }

    public void setReceiveBaofei (String receiveBaofei){
        this.receiveBaofei = receiveBaofei;
    }
    
    @ValueField(column = "receive_baofei")
    public String getReceiveBaofei(){
        return this.receiveBaofei;
    }

    public void setDaohuilv (String daohuilv){
        this.daohuilv = daohuilv;
    }
    
    @ValueField(column = "daohuilv")
    public String getDaohuilv(){
        return this.daohuilv;
    }

    public void setQiandanNum (String qiandanNum){
        this.qiandanNum = qiandanNum;
    }
    
    @ValueField(column = "qiandan_num")
    public String getQiandanNum(){
        return this.qiandanNum;
    }

    public void setQiandanlv (String qiandanlv){
        this.qiandanlv = qiandanlv;
    }
    
    @ValueField(column = "qiandanlv")
    public String getQiandanlv(){
        return this.qiandanlv;
    }

    public void setHuishoulv (String huishoulv){
        this.huishoulv = huishoulv;
    }
    
    @ValueField(column = "huishoulv")
    public String getHuishoulv(){
        return this.huishoulv;
    }

    public void setKehujingli (String kehujingli){
        this.kehujingli = kehujingli;
    }
    
    @ValueField(column = "kehujingli")
    public String getKehujingli(){
        return this.kehujingli;
    }

    public void setQiandanBaofei (String qiandanBaofei){
        this.qiandanBaofei = qiandanBaofei;
    }
    
    @ValueField(column = "qiandan_baofei")
    public String getQiandanBaofei(){
        return this.qiandanBaofei;
    }

    public void setRiqi (String riqi){
        this.riqi = riqi;
    }
    
    @ValueField(column = "riqi")
    public String getRiqi(){
        return this.riqi;
    }

    public void setBizplatTitle (String bizplatTitle){
        this.bizplatTitle = bizplatTitle;
    }
    
    @ValueField(column = "bizplat_title")
    public String getBizplatTitle(){
        return this.bizplatTitle;
    }

    public void setBizpaltId (Integer bizpaltId){
        this.bizpaltId = bizpaltId;
    }
    
    @ValueField(column = "bizpalt_id")
    public Integer getBizpaltId(){
        return this.bizpaltId;
    }


}