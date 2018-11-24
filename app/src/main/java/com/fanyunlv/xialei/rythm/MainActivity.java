package com.fanyunlv.xialei.rythm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.fragments.AudioFragment;
import com.fanyunlv.xialei.rythm.fragments.BaseFragment;
import com.fanyunlv.xialei.rythm.fragments.LocationFragment;
import com.fanyunlv.xialei.rythm.fragments.WifiFragment;
import com.fanyunlv.xialei.rythm.function.FunctionFragment;
import com.fanyunlv.xialei.rythm.sharedpreference.SharePrefUtil;
import com.fanyunlv.xialei.rythm.utils.FragmentUtil;
import com.fanyunlv.xialei.rythm.welcome.WelcomeFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "Rythm";

    private Timer timer;
    private TimerTask timerTask;


    private static int PERIOD_SHORT = 60 * 1000;
    private static int PERIOD_LONG = 600 * 1000;

    private SharePrefUtil sharePrefUtil;

    private AudioFragment audioFragment;
    private WifiFragment wifiFragment;
    private LocationFragment locationFragment;

    private FunctionFragment functionFragment;
    private WelcomeFragment welcomeFragment;

    private FragmentManager fragmentManager;
    private FragmentUtil fragmentUtil;


    private Handler mhandler;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: intent=" + intent.getAction());

            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                //updateTimeString();
            } else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)
                    ||intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                    ) {
//                updateWifiString();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                timerTask.cancel();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mhandler.sendEmptyMessage(2018);
                    }
                };
                timer.schedule(timerTask,1000,PERIOD_SHORT);

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                timerTask.cancel();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mhandler.sendEmptyMessage(2018);
                    }
                };
                timer.schedule(timerTask,1000,PERIOD_LONG);
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
//
//        mhandler = new currenthandler();

//        initReceiver();
//        inittimer();

        fragmentManager = getSupportFragmentManager();
        fragmentUtil = new FragmentUtil(fragmentManager);

        initFragments();
        sharePrefUtil = SharePrefUtil.getInstance(this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (sharePrefUtil.isFirstOpen()) {
            openwelcome();
        } else {
            if (fragmentUtil.getLastFragment()!=null) {

            }else {
                welComeFinished();
            }
        }
    }

    public void initFragments() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (welcomeFragment == null) {
            welcomeFragment = new WelcomeFragment();
        }
        if (functionFragment == null) {
            functionFragment = (FunctionFragment) Fragment.instantiate(this, FunctionFragment.class.getName());
        }
        if (audioFragment == null) {
            audioFragment = new AudioFragment();
        }
        if (wifiFragment == null) {
            wifiFragment = new WifiFragment();
        }
        if (locationFragment == null) {
            locationFragment = new LocationFragment();
        }

        //transaction.replace(R.id.content_view, new FunctionFragment());
        transaction.add(R.id.content_view, welcomeFragment, "welcome");
        transaction.add(R.id.content_view,audioFragment, "audio");
        transaction.add(R.id.content_view,wifiFragment, "wifi");
        transaction.add(R.id.content_view,locationFragment, "location");
        transaction.add(R.id.content_view,functionFragment,"function");

        transaction.hide(welcomeFragment);
        transaction.hide(audioFragment);
        transaction.hide(wifiFragment);
        transaction.hide(locationFragment);
        transaction.hide(functionFragment);

        transaction.commitNow();
    }

    public void onFragmentSelect(String name) {
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        Log.i(TAG, "onFragmentSelect 1 name ="+name);
        if (fragment != null) {
            Log.i(TAG, "onFragmentSelect 2");
            fragmentUtil.showNewFragment(name);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentUtil.isMainFragment()) {
            super.onBackPressed();
        }else {
            ((BaseFragment) fragmentUtil.getCurrentFragment()).onBackPressed();
        }
    }

    public void returnTomain() {
        fragmentUtil.returnTomain();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver,filter);
    }

//    private void  inittimer(){
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                mhandler.sendEmptyMessage(2018);
//            }
//        };
//        timer.schedule(timerTask,1000,PERIOD_LONG);
//    }


    @Override
    public void finish() {
        Log.i(TAG, "finish");
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        //unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void openwelcome(){
        fragmentUtil.showNewFragment("welcome");
    }
    public void welComeFinished() {
        fragmentUtil.showNewFragment("function");
    }
}
