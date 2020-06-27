package com.monash.mymoviememoirx.util;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.activity.MainActivity;

public class WatchlistAlarm extends BroadcastReceiver {

    private static final String TAG = "WatchlistAlarm";
    @Override
    // will be called when the alarm is fired
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: alarm fired");
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.watchlist_alarm);
        Button ok = dialog.findViewById(R.id.ok_button);
        Button ignore = dialog.findViewById(R.id.ignore_button);
        dialog.show();
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
