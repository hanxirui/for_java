package com.solaris.androidstone.service;

import android.content.Context;

import com.solaris.androidstone.model.DaoMaster;
import com.solaris.androidstone.model.DaoSession;
import com.solaris.androidstone.model.StudentDao;
import com.solaris.androidstone.model.TeacherDao;

/**
 * Created by hanxirui on 2016/12/23.
 */

public class DBService {
    private static final String DB_NAME = "greenDaoDemo.db";

    private DaoSession daoSession;

    public void init(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        daoSession = daoMaster.newSession();
    }

    public StudentDao getStudentDao(){
        return daoSession.getStudentDao();
    }

    public TeacherDao getTeacherDao(){
        return daoSession.getTeacherDao();
    }
}
