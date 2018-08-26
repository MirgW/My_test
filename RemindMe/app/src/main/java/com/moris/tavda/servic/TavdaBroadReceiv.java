package com.moris.tavda.servic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TavdaBroadReceiv extends BroadcastReceiver {
    final String LOG_TAG = "myLog";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: "+intent.getAction());
        context.startService(new Intent(context,TavdaService.class));
    }
}
