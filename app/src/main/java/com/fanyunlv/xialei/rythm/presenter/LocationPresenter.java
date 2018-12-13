package com.fanyunlv.xialei.rythm.presenter;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.interfaces.LocationIistener;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.DBhelper.OnDBchangedListener;
import com.fanyunlv.xialei.rythm.utils.TaskUtil;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/12/7.
 */
public class LocationPresenter implements OnDBchangedListener{
    private static final String TAG = LocationPresenter.class.getSimpleName();

    private static double DISTANCE_THRESHOLD = 100.00;

    private static LocationPresenter sPresener;

    private Context context;

    private ArrayList<LocationIistener> listeners;

    private ArrayList<TaskItems> taskItems;

    private BDLocation bdLocation;

    private DBhelper dBhelper;

    //baidu location
    public LocationClient mLocationClient = null;
    private MyLocationListener myLocationListener = new MyLocationListener();
    public BDNotifyListener myNotifyListener1 = new MyNotifiListener();
    public BDNotifyListener myNotifyListener2 = new MyNotifiListener();
    public BDNotifyListener myNotifyListener3 = new MyNotifiListener();

    private BDLocation mLastKnowLocation;

    private final double R_EARTH=6370996.81;  //地球的半径

    private LocationPresenter(Context context) {
        this.context = context;
        listeners = new ArrayList<>();
        dBhelper = DBhelper.getInstance(context);
        dBhelper.addListener(this);
        taskItems = gettasks();
    }

    public static LocationPresenter getInstance(Context context) {
        if (sPresener == null) {
            sPresener = new LocationPresenter(context);
        }
        return sPresener;
    }

    public void addLocationListenr(LocationIistener listener) {
        listeners.add(listener);
    }

    public void setNotifyLocation(int id,double lat,double longt,float radio) {
        if (id == 1) {
            myNotifyListener1.SetNotifyLocation(lat, longt, radio, mLocationClient.getLocOption().getCoorType());
        }else if (id == 2){
            myNotifyListener2.SetNotifyLocation(lat, longt, radio, mLocationClient.getLocOption().getCoorType());
        } else if (id == 3){
            myNotifyListener3.SetNotifyLocation(lat, longt, radio, mLocationClient.getLocOption().getCoorType());
        }
    }

    public void initBaiduLoaction() {
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(myLocationListener);
        //注册监听函数
//        mLocationClient.registerNotify(myNotifyListener);
        mLocationClient.registerNotify(myNotifyListener1);
        mLocationClient.registerNotify(myNotifyListener2);
        mLocationClient.registerNotify(myNotifyListener3);

        //myNotifyListener.SetNotifyLocation(40.0f, 116.0f, 3000, mLocationClient.getLocOption().getCoorType());
        //设置位置提醒，四个参数分别是：纬度、精度、半径、坐标类型

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(6000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(false);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(true);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedAddress(true);

        option.setIsNeedLocationDescribe(true);

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.startIndoorMode();
        mLocationClient.start();//开始定位
    }

    public void startFirstLocate() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(false);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(true);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            bdLocation = location;
            for (LocationIistener lis : listeners) {
                lis.onLocationReceived(location);
            }

            //check if we are close to a place
            checkDistance(bdLocation);
        }
    }

    public void checkDistance(BDLocation bdLocation) {
        if (taskItems.size() > 0) {
            //check distance
            for (TaskItems task : taskItems) {
                double dis = getDistance(bdLocation.getLatitude(), bdLocation.getLongitude(), getLocation(task.getCode()).getLati(), getLocation(task.getCode()).getLongti());
                if (dis <= DISTANCE_THRESHOLD) {
                    Log.i(TAG, "LineNum:165  Method:onReceiveLocation-->dis ="+dis);
                    TaskUtil.getInstance(context).handleTask(task);
                }
            }
        }
    }

    public class MyNotifiListener extends BDNotifyListener {
        @Override
        public void onNotify(BDLocation bdLocation, float v) {
            super.onNotify(bdLocation, v);
            Log.i(TAG, "LineNum:129  Method:onNotify--> ");
            for (LocationIistener lis : listeners) {
                lis.onLocationNeedNotify(bdLocation,v);
            }
        }
    }

    public BDLocation getLastKnowLocation() {
        return bdLocation;
    }

    public String getLastLocationDecri() {
        mLastKnowLocation = mLocationClient.getLastKnownLocation();
        if (mLastKnowLocation == null) {
            return "无";
        }
        return mLastKnowLocation.getLocationDescribe();
    }

    public double getDistance(BDLocation loc1,BDLocation loc2) {
        return getDistance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
    }

    /*
     * 获取两点间x,y轴之间的距离
     */
    public double getDistance(double lat1, double lng1,double lat2, double lng2) {
        double x = (lng2 - lng1)*Math.PI*R_EARTH*Math.cos(((lat1+lat2)/2)*Math.PI/180)/180;
        double y = (lat2 - lat1)*Math.PI*R_EARTH/180;
        return Math.hypot(x, y);   //得到两点之间的直线距离
    }

    public ArrayList<MyLocation> getLocations() {
        return  dBhelper.getLocationList();
    }

    public ArrayList<TaskItems> gettasks() {
        return dBhelper.getLocatiosTaskList();
    }

    public MyLocation getLocation(int code) {
        return dBhelper.getLocation(code);
    }

    @Override
    public void onDBchanged() {
        taskItems = gettasks();
    }
}
