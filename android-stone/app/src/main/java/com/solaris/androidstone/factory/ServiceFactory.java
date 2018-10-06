package com.solaris.androidstone.factory;

import com.solaris.androidstone.service.DBService;

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
