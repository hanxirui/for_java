package com.solaris.androidstone.utils;

import com.solaris.androidstone.model.WeatherBean;

/**
 * Created by hanxirui on 2016/12/24.
 */

public interface OnWeatherCallback {

    void onResponse(WeatherBean weatherBean);
    void onFailure(String error);
}
