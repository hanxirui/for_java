package com.solaris.androidstone.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.solaris.androidstone.BaseActivity;
import com.solaris.androidstone.R;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,NewsActivity.class);
        context.startActivity(intent);
    }
}
