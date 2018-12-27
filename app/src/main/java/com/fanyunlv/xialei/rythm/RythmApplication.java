package com.fanyunlv.xialei.rythm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmApplication extends Application {
    private final String TAG = "Rythm";

    public static final boolean ENABLE_LOG = false;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate: ");
        mContext = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public static Context getContext() {
        return mContext;
    }
}
