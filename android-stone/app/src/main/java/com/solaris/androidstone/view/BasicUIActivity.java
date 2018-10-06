package com.solaris.androidstone.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.solaris.androidstone.BaseActivity;
import com.solaris.androidstone.R;

import static com.solaris.androidstone.R.drawable.ic_school;

public class BasicUIActivity extends BaseActivity {

    private EditText editText;

    private ImageView imageView;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_ui);

        editText = (EditText) findViewById(R.id.editText);

        imageView = (ImageView) findViewById(R.id.imageView);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        Button btnShowMessage = (Button) findViewById(R.id.btnShowMessage);
        btnShowMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                Toast.makeText(BasicUIActivity.this, inputText, Toast.LENGTH_SHORT).show();

                imageView.setImageResource(ic_school);

                progressBar.setVisibility(View.GONE);

                AlertDialog.Builder dialog = new AlertDialog.Builder(BasicUIActivity.this);
                dialog.setTitle("This is Dialog");
                dialog.setMessage("Something important.");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();

            }
        });
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BasicUIActivity.class);
        context.startActivity(intent);
    }
}
