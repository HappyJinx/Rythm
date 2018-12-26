package com.fanyunlv.xialei.rythm.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.adapters.LocationlistAdapter;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.interfaces.LocationIistener;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.presenter.LocationPresenter;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.RythmDatabase;
import com.fanyunlv.xialei.rythm.utils.TaskUtil;

import java.util.ArrayList;

/**
 * Created by xialei on 2018/11/24.
 */
public class LocationFragment extends BaseFragment implements
        LocationIistener,
        View.OnClickListener,
        SensorEventListener,
        BaiduMap.OnMapClickListener,
        BaiduMap.OnMarkerClickListener, 
        OnGetGeoCoderResultListener,
        BaiduMap.OnMyLocationClickListener
{
    private static final String TAG = LocationFragment.class.getSimpleName();

//    private TextView locationinfo;
    private MapView bdmap_view;
    private BaiduMap baiduMap;

    private Button addlocation;
    private ImageButton centerSelf;
    private AlertDialog chooselocation;
    private LocationPresenter presenter;

    private BitmapDescriptor mNewMarker;
    private GeoCoder geoCoder;
    private LatLng mtouchPoint;
    private String policyName;
    private String mTouchGeoResultName;

    private BDLocation bdLocation;

    private SensorManager sensorManager;
    private float currentOrientation;

    private Context mcontext;

    public static ArrayList<MyLocation> locations;
    private ListView locationlist;
    private LocationlistAdapter adapter;

    private String[] locationnames = {
            "家",
            "公司",
            "超市",
            "地铁"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate ");
        mcontext = getContext();
        initdialog();
        presenter = LocationPresenter.getInstance(getContext());
        presenter.addLocationListenr(this);

        locations = new ArrayList<>();
        adapter = new LocationlistAdapter(getContext(), DBhelper.getInstance(getContext()).getLocationList());
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor send = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, send,SensorManager.SENSOR_DELAY_NORMAL);

        mNewMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    private void initdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加此定位为");
        builder.setSingleChoiceItems(locationnames, 0, new Locationchooselistener());
        chooselocation = builder.create();
    }

    @Override
    public void onDestroy() {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:100  Method:onDestroy--> ");
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onLocationReceived(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        bdLocation = location;

//        double latitude = location.getLatitude();    //获取纬度信息
//        double longitude = location.getLongitude();    //获取经度信息
//        float radius = location.getRadius();    //获取定位精度，默认值为0.0f
//        String coorType = location.getCoorType();
//        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//        int errorCode = location.getLocType();
//        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
//        String addr = location.getAddrStr();    //获取详细地址信息
//        String country = location.getCountry();    //获取国家
//        String province = location.getProvince();    //获取省份
//        String city = location.getCity();    //获取城市
//        String district = location.getDistrict();    //获取区县
//        String street = location.getStreet();    //获取街道信息
//
//        String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
//
//        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
//        if (location.getFloor() != null) {
//            if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:115  Method:onReceiveLocation--> ");
//            // 当前支持高精度室内定位
//            String buildingID = location.getBuildingID();// 百度内部建筑物ID
//            String buildingName = location.getBuildingName();// 百度内部建筑物缩写
//            String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
//            // 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
//        }

//        locationinfo.setText("当前定位信息：" +
//                "\n 经度："+latitude+
//                "\n 纬度："+longitude+
//                "\n 半径："+radius+" 米"+
//                "\n 地址："+addr+
//                "\n 描述："+locationDescribe);

//        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_gcoding);
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
//
////        LatLng latLng = new LatLng(latitude, longitude);
////        MarkerOptions markerOptions = new MarkerOptions().icon(mCurrentMarker).position(latLng);
////        baiduMap.clear();
////        baiduMap.addOverlay(markerOptions);
//        baiduMap.setMyLocationConfiguration(config);

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(20)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(currentOrientation)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);

    }

    @Override
    public void onLocationNeedNotify(BDLocation bdLocation, float v) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:112  Method:onLocationNeedNotify--> bdLocation="+bdLocation.getAddrStr());
        Toast.makeText(getContext(),"到达指定位置附近了~", Toast.LENGTH_SHORT).show();
    }

    int nums = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        //float[] values = event.values;
        //for (int i = 0; i < values.length; i++) {
        //   if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:174  Method:onSensorChanged--> "+values[0]);
        //}
        nums += 1;
        if (nums % 25 == 0) {
//            if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:183  Method:onSensorChanged--> nums="+nums);
            nums = 0;
            currentOrientation = event.values[0];
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(20)
                    .direction(currentOrientation)
                    .latitude(presenter.getLastKnowLocation().getLatitude())
                    .longitude(presenter.getLastKnowLocation().getLongitude()).build();
            baiduMap.setMyLocationData(locData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreateView ");

        View view = inflater.inflate(R.layout.location_fragment, container, false);
        bdmap_view = view.findViewById(R.id.baidu_mapview);
        baiduMap = bdmap_view.getMap();

        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);

        baiduMap.setMyLocationConfiguration(config);
        baiduMap.setMyLocationEnabled(true);// 开启定位图层
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(20)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0)
                .latitude(presenter.getLastKnowLocation().getLatitude())
                .longitude(presenter.getLastKnowLocation().getLongitude()).build();
        // 设置定位数据
        baiduMap.setMyLocationData(locData);

        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);
        baiduMap.setOnMyLocationClickListener(this);

        //缩放到指定位置和比例
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(new LatLng(presenter.getLastKnowLocation().getLatitude(),presenter.getLastKnowLocation().getLongitude()),18);
        baiduMap.animateMapStatus(mapStatusUpdate);

        addlocation = view.findViewById(R.id.add_location);
        addlocation.setOnClickListener(this);

        centerSelf = view.findViewById(R.id.center_self);
        centerSelf.setOnClickListener(this);

        locationlist = view.findViewById(R.id.location_list);
        locationlist.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_self:
                centerSelf();
                break;
            case R.id.add_location:
                chooselocation.show();
                break;
        }
    }
    
    /**
     *  description : center self with zoom 
     *  @author : xialei 
     *  date : 2018/12/15
     */
    public void centerSelf() {
        //缩放到指定位置和比例
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(new LatLng(presenter.getLastKnowLocation().getLatitude(),presenter.getLastKnowLocation().getLongitude()),18);
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.clear();
        mtouchPoint = null;
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:266  Method:onMapPoiClick--> mapPoi="+mapPoi.getName());
        mtouchPoint = mapPoi.getPosition();
        policyName = mapPoi.getName();
        mTouchGeoResultName = policyName;
        baiduMap.clear();
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mtouchPoint));
        
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:271  Method:onMapClick--> l ="+latLng.toString());
        mtouchPoint = latLng;
        MarkerOptions markerOptions = new MarkerOptions().icon(mNewMarker).position(latLng);
        baiduMap.clear();
        Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
        Bundle bundle = new Bundle();
        bundle.putParcelable("latlng",latLng);
        marker.setExtraInfo(bundle);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:292  Method:onMarkerClick--> ");
//        Bundle bundle = marker.getExtraInfo();
        LatLng latLng = marker.getExtraInfo().getParcelable("latlng");
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));

        return true;
    }

//    void onGetGeoCodeResult(GeoCodeResult var1);
//    void onGetReverseGeoCodeResult(ReverseGeoCodeResult var1);
    /**
     *  description : no call
     *  @author : xialei 
     *  date : 2018/12/15
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:324  Method:onGetGeoCodeResult--> r ="+geoCodeResult.toString());
    }


    /**
     *  description : get revers result, show info window
     *  @author : xialei
     *  date : 2018/12/15
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        ReverseGeoCodeResult.AddressComponent component = reverseGeoCodeResult.getAddressDetail();
        String miaoshu = reverseGeoCodeResult.getSematicDescription().split(",")[0];

        if (!TextUtils.isEmpty(policyName)) {
            miaoshu = policyName;
            policyName = "";
        }

        mTouchGeoResultName = miaoshu;

        String desc = component.district + component.street + component.streetNumber +"\n" +miaoshu;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.map_info_window,null,false);
        TextView addr = view.findViewById(R.id.info_map);
        addr.setText(desc);
        final InfoWindow infoWindow = new InfoWindow(view, mtouchPoint, -80);  //自定义view, 位置, y轴偏移量 (dp)
        baiduMap.showInfoWindow(infoWindow);
    }

    @Override
    public boolean onMyLocationClick() {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:360  Method:onMyLocationClick--> ");
        mtouchPoint = null;
        baiduMap.clear();
        return true;
    }

    /**
     *  description : insert location to database
     *  @author : xialei
     *  date : 2018/12/15
     */
    public class Locationchooselistener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onClick which ="+which);
//            MyLocation myLocation = new MyLocation(locationnames[which], bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getRadius(), bdLocation.getLocationDescribe());
            TaskUtil util = TaskUtil.getInstance(mcontext);
            if (mtouchPoint == null) {
                bdLocation = presenter.getLastKnowLocation();
                ContentValues values = new ContentValues();
                values.put(RythmDatabase.LOCATIONTABLE.NAME, locationnames[which]);
                values.put(RythmDatabase.LOCATIONTABLE.CODE, util.getcode(bdLocation));
                values.put(RythmDatabase.LOCATIONTABLE.LONGT, bdLocation.getLongitude());
                values.put(RythmDatabase.LOCATIONTABLE.LATI, bdLocation.getLatitude());
                values.put(RythmDatabase.LOCATIONTABLE.RADIOUS, bdLocation.getRadius());
                values.put(RythmDatabase.LOCATIONTABLE.DESCRIB, bdLocation.getLocationDescribe());
                DBhelper.getInstance(mcontext).insertLocation(values);

                TaskItems task = new TaskItems(locationnames[which], util.getcode(bdLocation), 0, 0, 0, 0);
                DBhelper.getInstance(mcontext).inserttaskDetails(task);

            }else {
                ContentValues values = new ContentValues();
                values.put(RythmDatabase.LOCATIONTABLE.NAME, locationnames[which]);
                values.put(RythmDatabase.LOCATIONTABLE.CODE, util.getcode(mtouchPoint));
                values.put(RythmDatabase.LOCATIONTABLE.LONGT, util.get6Num(mtouchPoint.longitude));
                values.put(RythmDatabase.LOCATIONTABLE.LATI, util.get6Num(mtouchPoint.latitude));
                values.put(RythmDatabase.LOCATIONTABLE.RADIOUS, 30.00);
                values.put(RythmDatabase.LOCATIONTABLE.DESCRIB, mTouchGeoResultName);
                DBhelper.getInstance(mcontext).insertLocation(values);

                TaskItems task = new TaskItems(locationnames[which], util.getcode(mtouchPoint), 0, 0, 0, 0);
                DBhelper.getInstance(mcontext).inserttaskDetails(task);
            }

            locations = DBhelper.getInstance(getContext()).getLocationList();
            adapter.setDatas(locations);
            adapter.notifyDataSetChanged();

            chooselocation.dismiss();
        }
    }


}
