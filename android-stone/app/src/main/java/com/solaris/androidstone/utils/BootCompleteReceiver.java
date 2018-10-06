package com.solaris.androidstone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"启动完成",Toast.LENGTH_LONG).show();
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
