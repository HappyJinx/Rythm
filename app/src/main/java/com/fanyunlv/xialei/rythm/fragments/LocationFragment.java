package com.fanyunlv.xialei.rythm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanyunlv.xialei.rythm.LocationlistAdapter;
import com.fanyunlv.xialei.rythm.MyLocation;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xialei on 2018/11/24.
 */
public class LocationFragment extends BaseFragment implements LocationListener,View.OnClickListener{
    private static final String TAG = LocationFragment.class.getSimpleName();

    private LocationManager locationManager;
    private TextView locationinfo;
    private Button addlocation;
    private Location location;
    public static ArrayList<MyLocation> locations;
    private ListView locationlist;
    private LocationlistAdapter adapter;

    private AlertDialog chooselocation;

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
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locations = new ArrayList<>();
        //has checked permission before launch this fragment
        initdialog();
        adapter = new LocationlistAdapter(getContext(), DBhelper.getInstance(getContext()).getLocationList());

    }

    @Override
    public void onResume() {
        super.onResume();
        String pro = getProvider(locationManager);
        if (pro != null) {
            locationManager.requestLocationUpdates(pro, 1000 * 10, 20, this);
        }
    }

    private void initdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加此定位为");
        builder.setSingleChoiceItems(locationnames, 0, new Locationchooselistener());
        chooselocation = builder.create();
    }

    public class Locationchooselistener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i(TAG, "onClick which ="+which);
            DBhelper.getInstance(getContext()).insertLocation(new MyLocation(locationnames[which], location.getLongitude(),location.getLatitude()));
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
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged ");
        this.location = location;
        double longt = location.getLongitude();
        double lat = location.getLatitude();
        double alt = location.getAltitude();
        locationinfo.setText("当前定位信息 \n 海拔: "+alt+"\n 经度: "+longt+"\n 纬度: "+lat);
        Toast.makeText(getContext(), "位置信息改变了~", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled ");
    }

    @Override
    public void onClick(View v) {
//        locations.add(location);
        chooselocation.show();
    }
}
