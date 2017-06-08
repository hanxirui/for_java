package com.solaris.android_third_party.service;

/**
 * Created by hanxirui on 2016/12/24.
 */


import com.solaris.android_third_party.model.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @DateTime: 2016-07-08 11:08
 * @Author: duke
 * @Deacription: 天气的Retrofit服务接口
 */
public interface IWeatherService {

    /**
     * Retrofit的原始用法
     * @return
     */
    @GET("data/cityinfo/101010100.html")
    Call<WeatherBean> getWeather();

    /**
     * Rxjava的用法
     * @return
     */
    @GET("data/cityinfo/101010100.html")
    Observable<WeatherBean> getWeatherRxJava();

    /*@POST("data/cityinfo/101010100.html")
    Observable<WeatherBean> getWeather();*/
}
