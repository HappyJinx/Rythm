package com.fanyunlv.xialei.rythm.presenter;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.DBhelper.OnDBchangedListener;

import java.util.List;

/**
 * Created by admin on 2018/8/22.
 */

public class WifiCheckPresenter implements OnDBchangedListener {
    public static WifiCheckPresenter sWifiCheckPresenter;

    private Context context;
    private WifiManager wifiManager;
    private DBhelper dBhelper;
    private List<String> list;

    private int checkcoun = 0;
    private final int COUNT_THREALD = 3;

    private WifiCheckPresenter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        dBhelper = DBhelper.getInstance(context);
        dBhelper.addListener(this);
        list = dBhelper.getWifiList();
    }

    public static WifiCheckPresenter getInstance(Context context) {
        if (sWifiCheckPresenter == null) {
            sWifiCheckPresenter = new WifiCheckPresenter(context);
        }
        return sWifiCheckPresenter;
    }

    public void enableWifi(boolean open) {
        wifiManager.setWifiEnabled(open);
    }

    public String getSSIDname() {
        if (!wifiManager.isWifiEnabled()) {
            return context.getResources().getString(R.string.wifi_disable);
        }
        String name = wifiManager.getConnectionInfo().getSSID().replaceAll("\"", "");
        if ("<unknown ssid>".equals(name)) {
            return context.getResources().getString(R.string.none_wifi);
        }
        return wifiManager.getConnectionInfo().getSSID().replaceAll("\"","");
    }

    public void handlewifi() {
        if (list.size() > 0) {
            if (!wifiManager.isWifiEnabled()&& checkcoun != 0) {
                enableWifi(true);
                return;
            }
            if (checkcoun == COUNT_THREALD) {
                checkcoun = 0;
                enableWifi(false);
                return;
            }
            for (String item : list) {
                if (getSSIDname().contains(item)) {
                    checkcoun += 1;
                    wifiManager.disconnect();
                    break;
                }
            }
        }
    }

    public void addNewBlackWifi(String name) {
        DBhelper.getInstance(context).insertwifi(name);
    }

    @Override
    public void onDBchanged() {
        list = dBhelper.getWifiList();
    }
}
