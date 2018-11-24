package com.fanyunlv.xialei.rythm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i("Rythm", "MainReceiver onReceive: action ="+intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_MAIN)) {
            Intent intent1 = new Intent("android.intent.action.MAIN");
            intent1.setPackage("com.fanyunlv.xialei.rythm");
            context.startActivity(intent1);
        } else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){

        }
    }
}
