package com.deta.androidlib.activity;

import android.app.Activity;
import android.os.Bundle;

import com.deta.androidlib.net.RequestManager;

public abstract class BaseActivity extends Activity {

    /**
     * 请求列表管理器
     */
    protected RequestManager requestManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestManager = new RequestManager(this);
        super.onCreate(savedInstanceState);

        initVariables();
        initViews(savedInstanceState);
        loadData();

    }

    //    初始化变量,包括Intent带的数据和Activity内的变量
    protected abstract void initVariables();

    //    加载layout布局文件,初始化控件,为控件挂上事件方法
    protected abstract void initViews(Bundle savedInstanceState);

    //    调用MobileAPI获取数据
    protected abstract void loadData();

    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onDestroy();
    }

    protected void onPause() {
        /**
         * 在activity停止的时候同时设置停止请求，停止线程请求回调
         */
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onPause();
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
