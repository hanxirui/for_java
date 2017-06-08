package com.solaris.android_third_party.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.R;

public class HiddenIntentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_intent);
    }

}
