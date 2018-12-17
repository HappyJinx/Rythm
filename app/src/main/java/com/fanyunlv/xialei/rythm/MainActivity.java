package com.fanyunlv.xialei.rythm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanyunlv.xialei.rythm.activitys.FunctionActivity;
import com.fanyunlv.xialei.rythm.fragments.AudioFragment;
import com.fanyunlv.xialei.rythm.fragments.WifiFragment;
import com.fanyunlv.xialei.rythm.fragments.FunctionFragment;
import com.fanyunlv.xialei.rythm.presenter.LocationPresenter;
import com.fanyunlv.xialei.rythm.presenter.RingmodePresenter;
import com.fanyunlv.xialei.rythm.presenter.WifiCheckPresenter;
import com.fanyunlv.xialei.rythm.utils.SharePrefUtil;
import com.fanyunlv.xialei.rythm.utils.FragmentUtil;
import com.fanyunlv.xialei.rythm.utils.PermissionUtil;
import com.fanyunlv.xialei.rythm.fragments.WelcomeFragment;


public class MainActivity extends AppCompatActivity{
    public static final String TAG = "Rythm";

    private SharePrefUtil sharePrefUtil;
    private FragmentManager fragmentManager;
    private FragmentUtil fragmentUtil;
    private PermissionUtil permissionUtil;

    private ActionBar actionBar;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onReceive: intent=" + intent.getAction());

            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                if (fragmentUtil.getCurrentFragment() instanceof AudioFragment) {
//                    RingmodePresenter.getInstance(context).checkTimeTask();
                }
            } else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)
                    ||intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                    ) {
                if (fragmentUtil.getCurrentFragment() instanceof WifiFragment) {
                    WifiCheckPresenter.getInstance(context).forbidWifi();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        actionBar = getSupportActionBar();

        fragmentManager = getSupportFragmentManager();
        fragmentUtil = new FragmentUtil(fragmentManager);
        sharePrefUtil = SharePrefUtil.getInstance(this);
        permissionUtil = PermissionUtil.getInstance(this);
        if (!permissionUtil.checkHaspermissions()) {
            permissionUtil.requestPermissions(10);
        }

        showContent();
        initReceiver();
        //init baidu location service
        LocationPresenter.getInstance(getApplicationContext()).setLocationMode(LocationPresenter.ONCE_MODE);
    }

    public void setTitle(int resid) {
        actionBar.setTitle(resid);
    }
    public void hideActionbar(boolean hide) {
        if (hide) {
            actionBar.hide();
        }else if (!actionBar.isShowing()){
            actionBar.show();
        }
    }
    public void showContent() { //main content
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (sharePrefUtil.isFirstOpen()) {
            transaction.add(R.id.content_view,new WelcomeFragment(), "welcome");
        } else if(fragmentUtil.getLastFragment()==null) {
            transaction.add(R.id.content_view,new FunctionFragment(),"function");
        }
        transaction.commitNow();
    }

    public void onFragmentSelect(String name) {
        Intent intent = new Intent("com.android.rhythm.function");
        intent.putExtra(FunctionActivity.FRGMENT_TAG, name);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onRequestPermissionsResult ");
        if (requestCode == 10) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //permissionUtil.requestPermissions(10);
                    if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:108  Method:onRequestPermissionsResult--> granted");
                }
            }
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver,filter);
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    public void welComeFinished() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_view,new FunctionFragment(),"function");
        transaction.commitNow();
    }

}
