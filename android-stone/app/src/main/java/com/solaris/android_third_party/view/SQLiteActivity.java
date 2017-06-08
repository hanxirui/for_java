package com.solaris.android_third_party.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.solaris.android_third_party.R;
import com.solaris.android_third_party.utils.MyDatabaseHelper;

public class SQLiteActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,SQLiteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
        Button createDatabase = (Button)findViewById(R.id.btnCreateDb);

        createDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();

            }
        });

        Button btnAddData = (Button)findViewById(R.id.btnAddData);
        btnAddData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
//                第一条数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("Book",null,values);
                values.clear();
                //                第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                db.insert("Book",null,values);
                values.clear();
            }
        });

        Button btnUpdateData = (Button)findViewById(R.id.btnUpdateData);
        btnUpdateData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                db.update("Book",values,"name = ?",new String[]{"The Da Vinci Code"});
            }
        });

        Button btnDeleteData = (Button)findViewById(R.id.btnDeleteData);
        btnDeleteData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book","pages > ?",new String[]{"500"});
            }
        });

        Button btnQueryData = (Button)findViewById(R.id.btnQueryData);
        btnQueryData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db  = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);

                if(cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d("SQLiteActivity","book name is "+name);
                        Log.d("SQLiteActivity","book author is "+author);
                        Log.d("SQLiteActivity","book pages is "+pages);
                        Log.d("SQLiteActivity","book price is "+price);
                    }while(cursor.moveToNext());
                }

                cursor.close();
            }
        });
    }
}
