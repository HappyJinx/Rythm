package com.fanyunlv.xialei.rythm.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.fragments.AudioFragment;
import com.fanyunlv.xialei.rythm.fragments.WifiFragment;
import com.fanyunlv.xialei.rythm.fragments.LocationFragment;
import com.fanyunlv.xialei.rythm.utils.FragmentUtil;
import com.fanyunlv.xialei.rythm.utils.PermissionUtil;

import java.util.HashMap;

/**
 * Created by xialei on 2018/12/7.
 */
public class FunctionActivity extends AppCompatActivity {

    private static final String TAG = FunctionActivity.class.getSimpleName();

    private AudioFragment audioFragment;
    private WifiFragment wifiFragment;
    private LocationFragment locationFragment;
    private FragmentManager fragmentManager;

    private String fragName;

    private PermissionUtil permissionUtil;

    private HashMap<String, Fragment> hashMap = new HashMap();

    public static final String FRGMENT_TAG ="frgment_tag";

    private static final String[] LOCATIONS = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager = getSupportFragmentManager();
        permissionUtil = PermissionUtil.getInstance(this);

        initfragments();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "LineNum:36  Method:onNewIntent--> ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent startIntent = getIntent();
        fragName = startIntent.getStringExtra(FRGMENT_TAG);
        showFunction(fragName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4) {
            for (int i=0;i<grantResults.length; i++){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions,requestCode);
                    return;
                }
            }
            showFunction(fragName);
        }
    }

    public void showFunction(String fragName) {
        Fragment fragment = hashMap.get(fragName);
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_view, hashMap.get(fragName));
        transaction.commitNow();
    }

    public void initfragments() {
        audioFragment = new AudioFragment();
        wifiFragment = new WifiFragment();
        locationFragment = new LocationFragment();
        String[] fragments = getResources().getStringArray(R.array.fragments_list);
        hashMap.put(fragments[0], audioFragment);
        hashMap.put(fragments[1], wifiFragment);
        hashMap.put(fragments[2], locationFragment);
    }

}
