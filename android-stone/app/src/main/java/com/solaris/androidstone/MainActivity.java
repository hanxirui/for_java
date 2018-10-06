package com.solaris.androidstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;

import com.solaris.androidstone.view.BroadcastActivity;
import com.solaris.androidstone.view.ContentResolverActivity;
import com.solaris.androidstone.view.FirstcodeActivity;
import com.solaris.androidstone.view.GlideActivity;
import com.solaris.androidstone.view.NewsActivity;
import com.solaris.androidstone.view.NotificationActivity;
import com.solaris.androidstone.view.RealmActivity;
import com.solaris.androidstone.view.RetrofitActivity;
import com.solaris.androidstone.view.SQLiteActivity;
import com.solaris.androidstone.view.StorageActivity;
import com.solaris.androidstone.view.StudentActivity;
import com.solaris.androidstone.view.UIActivity;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnStudent = (FloatingActionButton) findViewById(R.id.btnGreenDao);
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStudentActivity();
            }
        });

        FloatingActionButton btnRetrofit = (FloatingActionButton) findViewById(R.id.btnRetrofix);
        btnRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRetrofitActivity();
            }
        });


        Button btnRealm = (Button) findViewById(R.id.btnRealm);
        btnRealm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRealmActivity();
            }
        });

        Button btnFirstcode = (Button) findViewById(R.id.firstcode);
        btnFirstcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirstcodeActivity.StartActivity(MainActivity.this, "", "");
            }
        });

        FloatingActionButton btnGlide = (FloatingActionButton) findViewById(R.id.btnGlide);
        btnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGlideActivity();
            }
        });

        Button btnUI = (Button) findViewById(R.id.btnUI);
        btnUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIActivity.startActivity(MainActivity.this);
            }
        });

        Button btnFrg = (Button) findViewById(R.id.btnFrg);
        btnFrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.startActivity(MainActivity.this);
            }
        });

        Button btnBroadcast = (Button)findViewById(R.id.broadcast);
        btnBroadcast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BroadcastActivity.startActivity(MainActivity.this);
            }
        });

        Button btnSendBCT = (Button)findViewById(R.id.btnSendBCT);
        btnSendBCT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");

                sendBroadcast(intent);
            }
        });

        Button btnForceOffline = (Button)findViewById(R.id.btn_offline);
        btnForceOffline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });

        Button btnShowStorage = (Button)findViewById(R.id.btnShowStorage);
        btnShowStorage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StorageActivity.startActivity(MainActivity.this);
            }
        });

        Button btnSQLite = (Button)findViewById(R.id.btnSQLite);
        btnSQLite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteActivity.startActivity(MainActivity.this);
            }
        });

        Button btnContent = (Button)findViewById(R.id.btnContent);
        btnContent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContentResolverActivity.class);
                startActivity(intent);
            }
        });

        Button btnNotification = (Button)findViewById(R.id.btnNotifi);
        btnNotification.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }


    private void startStudentActivity() {
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }

    private void startRetrofitActivity() {
        Intent intent = new Intent(this, RetrofitActivity.class);
        startActivity(intent);
    }

    private void startRealmActivity() {
        Intent intent = new Intent(this, RealmActivity.class);
        startActivity(intent);
    }

    private void startGlideActivity() {
        Intent intent = new Intent(this, GlideActivity.class);
        startActivity(intent);
    }

}
