package com.solaris.android_third_party.presenter;

/**
 * Created by hanxirui on 2016/12/24.
 */

import com.solaris.android_third_party.model.WeatherBean;
import com.solaris.android_third_party.utils.IWeatherModel;
import com.solaris.android_third_party.utils.OnWeatherCallback;
import com.solaris.android_third_party.utils.WeatherRxJavaModelImpl;
import com.solaris.android_third_party.view.RetrofitActivity;

/**
 * @DateTime: 2016-07-08 10:31
 * @Author: duke
 * @Deacription: presenter实现类
 */
public class WeatherPresenterImpl implements IWeatherPresenter, OnWeatherCallback {
    private RetrofitActivity iMainActivity;
    private IWeatherModel iWeatherModel;

    public WeatherPresenterImpl(RetrofitActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        //iWeatherModel = new WeatherNetModelImpl(this);
        iWeatherModel = new WeatherRxJavaModelImpl(this);
    }

    @Override
    public void getWeather() {
        iWeatherModel.getWeather();
    }

    @Override
    public void onResponse(WeatherBean weatherBean) {
        iMainActivity.setTextShow(weatherBean.weatherinfo.toString());
    }

    @Override
    public void onFailure(String error) {
        iMainActivity.setTextShow(error);
    }
}
