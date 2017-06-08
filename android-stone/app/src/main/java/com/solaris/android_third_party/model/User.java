package com.solaris.android_third_party.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hanxirui on 2016/12/21.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String           name;
    private int              age;
    private RealmList<User> friends;
    @Ignore
    private int              sessionId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RealmList<User> getFriends() {
        return friends;
    }

    public void setFriends(RealmList<User> friends) {
        this.friends = friends;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}

//    @PrimaryKey 顧名思義
//    @Ignore 不會存入資料庫的成員變數
//@Index 可以加快Query的速度，但相對的insert會變得緩慢
