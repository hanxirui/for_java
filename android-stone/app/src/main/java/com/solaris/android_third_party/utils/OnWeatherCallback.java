package com.solaris.android_third_party.utils;

import com.solaris.android_third_party.model.WeatherBean;

/**
 * Created by hanxirui on 2016/12/24.
 */

public interface OnWeatherCallback {

    void onResponse(WeatherBean weatherBean);
    void onFailure(String error);
}
