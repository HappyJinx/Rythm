package com.fanyunlv.xialei.rythm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmApplication extends Application {
    private final String TAG = "Rythm";

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }


}
