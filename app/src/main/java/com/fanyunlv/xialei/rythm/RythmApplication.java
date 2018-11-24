package com.fanyunlv.xialei.rythm;

import android.app.Application;
import android.util.Log;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmApplication extends Application {
    private final String TAG = "Rythm";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

}
