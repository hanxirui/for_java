package com.solaris.androidstone.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.solaris.androidstone.BaseActivity;
import com.solaris.androidstone.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StorageActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, StorageActivity.class);
        context.startActivity(intent);
    }

    private EditText txtContent;

    private Button btnSave;

    private Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        txtContent = (EditText) findViewById(R.id.txtContent);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = txtContent.getText().toString();
                save(content);
            }
        });

        btnLoad = (Button)findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String content = txtContent.getText().toString();
        save(content);
    }

    public void save(String txt) {
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(txt);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();

        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            Toast.makeText(StorageActivity.this,content.toString(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
        return content.toString();
    }
}
