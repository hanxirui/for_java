package com.solaris.androidstone.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.solaris.androidstone.BaseActivity;
import com.solaris.androidstone.R;
import com.solaris.androidstone.adapter.MsgAdapter;
import com.solaris.androidstone.model.Msg;

import java.util.ArrayList;
import java.util.List;

public class TalkActivity extends BaseActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,TalkActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        initMsgs();

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);

        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);

                    adapter.notifyItemInserted(msgList.size()-1);

                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }

    private void initMsgs(){
        Msg msg1 = new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guy.",Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("HelHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guylo guy.",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
        Msg msg4 = new Msg("Hello guHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guyHello guyy.",Msg.TYPE_SEND);
        msgList.add(msg4);
    }
}
