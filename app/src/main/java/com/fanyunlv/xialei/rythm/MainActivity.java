package com.fanyunlv.xialei.rythm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fanyunlv.xialei.rythm.sharedpreference.SharePrefUtil;
import com.fanyunlv.xialei.rythm.welcome.WelcomeActivity;
import com.fanyunlv.xialei.rythm.welcome.WelcomeFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "Rythm";
    private TextView ringmodetext;
    private TextView currenttime;
    private TextView setchangeTime;
    private Button setTimeBttn;

    private TextView wifinametext;
    private TextView black_wifi;
    private Button setwifi_btn;

    private RingmodePresenter ringmodePresenter;
    private WifiCheckPresenter wifiCheckPresenter;

    private Timer timer;
    private TimerTask timerTask;
    private Handler mhandler;

    private static int PERIOD_SHORT = 60 * 1000;
    private static int PERIOD_LONG = 600 * 1000;

    private SharePrefUtil sharePrefUtil;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settime_btn:
                startActivity(new Intent(MainActivity.this,SettimeActivity.class));
                break;
            case R.id.setwifi_btn:
                startActivity(new Intent(MainActivity.this,SetwifiActivity.class));
                break;
        }
    }


    public class currenthandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: handle");
            updateTimeString();
            updateWifiString();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: intent=" + intent.getAction());

            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                updateTimeString();
            } else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)
                    ||intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                    ) {
                updateWifiString();
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

    private void updateTimeString() {
        ringmodePresenter.handletime();
        ringmodetext.setText(ringmodePresenter.getCurrentMode());
        currenttime.setText(ringmodePresenter.getTime());
        setchangeTime.setText(DBhelper.getInstance(this).getSelectedTime());
    }

    private void updateWifiString() {
        wifiCheckPresenter.handlewifi();
        wifinametext.setText(wifiCheckPresenter.getSSIDname());
        black_wifi.setText(DBhelper.getInstance(this).getSelectedWifi());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
        initviews();
        ringmodePresenter = RingmodePresenter.getInstance(this);
        wifiCheckPresenter = WifiCheckPresenter.getInstance(this);
        mhandler = new currenthandler();
        if (!((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        initReceiver();
        inittimer();

        sharePrefUtil = SharePrefUtil.getInstance(this);
    }

    public void welComeFinished() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_view, new FunctionFragment());
        transaction.commitNowAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTimeString();
        updateWifiString();
        if (sharePrefUtil.isFirstOpen()) {
            openwelcome();
        }
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

    private void initviews() {
        ringmodetext = (TextView) findViewById(R.id.ringmodetext);
        currenttime = (TextView) findViewById(R.id.current_time);
        setchangeTime = (TextView) findViewById(R.id.set_time);
        setTimeBttn = (Button) findViewById(R.id.settime_btn);
        setTimeBttn.setOnClickListener(this);


        wifinametext = (TextView) findViewById(R.id.wifinametext);
        black_wifi = (TextView) findViewById(R.id.black_wifi);
        setwifi_btn = (Button) findViewById(R.id.setwifi_btn);
        setwifi_btn.setOnClickListener(this);


        
    }

    private void  inittimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mhandler.sendEmptyMessage(2018);
            }
        };
        timer.schedule(timerTask,1000,PERIOD_LONG);
    }


    @Override
    public void finish() {
        Log.i(TAG, "finish");
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void openwelcome(){
        Log.i(TAG, "openwelcome ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        transaction.add(R.id.content_view, welcomeFragment);
        transaction.commitAllowingStateLoss();
    }
}
