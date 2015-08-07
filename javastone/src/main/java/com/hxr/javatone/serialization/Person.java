package com.hxr.javatone.serialization;


import java.util.Date;
import java.util.List;

import com.dyuproject.protostuff.Tag;

public class Person {
    @Tag(alias="username", value = 1)
    private  String name;
    @Tag(alias="Motto", value = 2)
    private  String motto ;
    @Tag(alias="gender", value = 3)
    private  Gender gender = Gender.MALE;   
    @Tag(alias="userlist1", value = 4)
    private List<User> userlist ;
    @Tag(alias="date", value = 5)
    private  Date date ;
    
    enum Gender {  
        MALE(1) ,    FEMALE(2);
        private Gender(final int value){
            this.value = value ;
        }       
        private final int value;      
        @Override
        public String toString(){
             return super.toString()+"("+value+")";  
        }
    }
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getMotto() {
        return motto;
    }
    public void setMotto(final String motto) {
        this.motto = motto;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(final Gender gender) {
        this.gender = gender;
    }
    public List<User> getUserlist() {
        return userlist;
    }
    public void setUserlist(final List<User> userlist) {
        this.userlist = userlist;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(final Date date) {
        this.date = date;
    }
    
}
