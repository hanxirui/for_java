package com.solaris.android_third_party.service;

/**
 * Created by hanxirui on 2016/12/24.
 */

import com.solaris.android_third_party.utils.HttpServerConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @DateTime: 2016-07-08 15:56
 * @Author: duke
 * @Deacription:  retrofit管理类
 */
public class RetrofitManager {
    private volatile static RetrofitManager mInstance;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private RetrofitManager() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS)
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .build();
        }

        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(HttpServerConfig.server)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//用于返回Rxjava调用,非必须
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
    }

    public static RetrofitManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null)
                    mInstance = new RetrofitManager();
            }
        }
        return mInstance;
    }

    public <T> T createService(Class<T> clz) {
        return retrofit.create(clz);
    }
}
