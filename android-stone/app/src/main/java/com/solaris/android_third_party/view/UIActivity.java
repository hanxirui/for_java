package com.solaris.android_third_party.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.R;

public class UIActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        Button btnBasicUI = (Button) findViewById(R.id.btnBasicUI);
        btnBasicUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUIActivity.startActivity(UIActivity.this);
            }
        });

        Button btnLayout = (Button) findViewById(R.id.btnLayout);
        btnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewActivity.startActivity(UIActivity.this);
            }
        });

        Button btnLayout1 = (Button) findViewById(R.id.btnLayout1);
        btnLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView2Activity.startActivity(UIActivity.this);
            }
        });

        Button btnLayout2 = (Button) findViewById(R.id.btnLayout2);
        btnLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView3Activity.startActivity(UIActivity.this);
            }
        });

        Button btnLayout3 = (Button) findViewById(R.id.btnLayout3);
        btnLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TalkActivity.startActivity(UIActivity.this);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UIActivity.class);
        context.startActivity(intent);

    }
}
