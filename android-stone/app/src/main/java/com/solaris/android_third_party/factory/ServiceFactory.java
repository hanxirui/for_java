package com.solaris.android_third_party.factory;

import com.solaris.android_third_party.service.DBService;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class ServiceFactory {
    private ServiceFactory(){

    }

    private static DBService dbService;

    public static DBService getDbService(){
        if(dbService == null){
            dbService = new DBService();
        }

        return dbService;
    }
}
