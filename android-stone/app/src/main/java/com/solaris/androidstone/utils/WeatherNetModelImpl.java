package com.solaris.androidstone.utils;

/**
 * Created by hanxirui on 2016/12/24.
 */


import com.solaris.androidstone.model.WeatherBean;
import com.solaris.androidstone.service.IWeatherService;
import com.solaris.androidstone.service.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @DateTime: 2016-07-08 09:57
 * @Author: duke
 * @Deacription: model层实现类,Retrofit方式
 */
public class WeatherNetModelImpl implements IWeatherModel, Callback<WeatherBean> {

    private OnWeatherCallback listener;

    public WeatherNetModelImpl(OnWeatherCallback callback) {
        this.listener = callback;
    }

    @Override
    public void getWeather() {
        RetrofitManager.getInstance().createService(IWeatherService.class).getWeather().enqueue(this);
    }

    @Override
    public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
        WeatherBean weather = response.body();
        if (listener != null)
            listener.onResponse(weather);
    }

    @Override
    public void onFailure(Call<WeatherBean> call, Throwable t) {
        if (listener != null)
            listener.onFailure(t.getMessage());
    }
}