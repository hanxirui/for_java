package com.solaris.androidstone.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solaris.androidstone.BaseActivity;
import com.solaris.androidstone.R;
import com.solaris.androidstone.presenter.IWeatherPresenter;
import com.solaris.androidstone.presenter.WeatherPresenterImpl;

/**
 * Created by hanxirui on 2016/12/24.
 */

/**
 * View层实现类
 */
public class RetrofitActivity extends BaseActivity {
    private Button search;
    private TextView show;

    private IWeatherPresenter presenter = new WeatherPresenterImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        show = (TextView) findViewById(R.id.show);
        search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                presenter.getWeather();
            }
        });
    }




    public void setTextShow(String weatherString) {
        show.setText(weatherString);
    }
}
