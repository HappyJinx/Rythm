package com.fanyunlv.xialei.rythm.location;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.fanyunlv.xialei.rythm.LocationIistener;
import com.fanyunlv.xialei.rythm.fragments.BaseFragment;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialei on 2018/11/24.
 */
public class LocationFragment extends BaseFragment implements LocationIistener,View.OnClickListener{
    private static final String TAG = LocationFragment.class.getSimpleName();

    private TextView locationinfo;
    private Button addlocation;
    private AlertDialog chooselocation;
    private LocationPresenter presenter;

    private BDLocation bdLocation;


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
        Log.i(TAG, "onCreate ");
        //has checked permission before launch this fragment
        initdialog();
        presenter = LocationPresenter.getInstance(getContext());
        presenter.addLocationListenr(this);

        locations = new ArrayList<>();
        adapter = new LocationlistAdapter(getContext(), DBhelper.getInstance(getContext()).getLocationList());

    }

    private void initdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加此定位为");
        builder.setSingleChoiceItems(locationnames, 0, new Locationchooselistener());
        chooselocation = builder.create();
    }

    @Override
    public void onLocationReceived(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        bdLocation = location;

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f
        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息

        String locationDescribe = location.getLocationDescribe();    //获取位置描述信息

        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        if (location.getFloor() != null) {
            Log.i(TAG, "LineNum:115  Method:onReceiveLocation--> ");
            // 当前支持高精度室内定位
            String buildingID = location.getBuildingID();// 百度内部建筑物ID
            String buildingName = location.getBuildingName();// 百度内部建筑物缩写
            String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
            // 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
        }

        locationinfo.setText("当前定位信息：" +
                "\n 经度："+latitude+
                "\n 纬度："+longitude+
                "\n 半径："+radius+" 米"+
                "\n 地址："+addr+
                "\n 描述："+locationDescribe);

        //notify this location
        for (int i = 0; i < locations.size(); i++) {
            MyLocation myLocation = locations.get(i);
            Log.i(TAG, "LineNum:117  Method:onLocationReceived--> ");
            presenter.setNotifyLocation(i,myLocation.getLati(),myLocation.getLongti(),(float)myLocation.getRadios());
        }
    }

    @Override
    public void onLocationNeedNotify(BDLocation bdLocation, float v) {
        Log.i(TAG, "LineNum:112  Method:onLocationNeedNotify--> bdLocation="+bdLocation.getAddrStr());
        Toast.makeText(getContext(),"到达指定位置附近了~", Toast.LENGTH_SHORT).show();
    }

    public class Locationchooselistener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i(TAG, "onClick which ="+which);

            DBhelper.getInstance(getContext()).insertLocation(new MyLocation(locationnames[which],bdLocation.getLatitude(),bdLocation.getLongitude(),bdLocation.getRadius()));
            locations = DBhelper.getInstance(getContext()).getLocationList();
            adapter.setDatas(locations);
            adapter.notifyDataSetChanged();

            chooselocation.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView ");

        View view = inflater.inflate(R.layout.location_fragment, container, false);
        locationinfo = view.findViewById(R.id.location_info);
        addlocation = view.findViewById(R.id.add_location);
        addlocation.setOnClickListener(this);
        locationlist = view.findViewById(R.id.location_list);
        locationlist.setAdapter(adapter);
        return view;
    }

    private String getProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;//网络定位
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        chooselocation.show();
    }
}
