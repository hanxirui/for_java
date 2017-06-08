package com.solaris.android_third_party;

import android.app.Application;
import android.content.Context;

import com.solaris.android_third_party.factory.ServiceFactory;
import com.solaris.android_third_party.service.DBService;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class ThirdPartyShowApplication extends Application {

    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();

        DBService dbService = ServiceFactory.getDbService();
        dbService.init(this);

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
