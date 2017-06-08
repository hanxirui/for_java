package com.solaris.android_third_party.utils;

/**
 * Created by hanxirui on 2016/12/24.
 */


import com.solaris.android_third_party.model.WeatherBean;
import com.solaris.android_third_party.service.IWeatherService;
import com.solaris.android_third_party.service.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @DateTime: 2016-07-08 09:57
 * @Author: duke
 * @Deacription:  model层实现类,Retrofit + RxJava + RxAndroid
 */
public class WeatherRxJavaModelImpl implements IWeatherModel {

    private OnWeatherCallback listener;

    public WeatherRxJavaModelImpl(OnWeatherCallback callback) {
        this.listener = callback;
    }

    @Override
    public void getWeather() {
        RetrofitManager.getInstance().createService(IWeatherService.class).getWeatherRxJava()
                .subscribeOn(Schedulers.io())//这连续几个方法都是RxJava里面的
                .observeOn(AndroidSchedulers.mainThread())//AndroidSchedulers是RxAndroid里面的类
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null)
                            listener.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherBean weather) {
                        if (listener != null)
                            listener.onResponse(weather);
                    }
                });
    }
}