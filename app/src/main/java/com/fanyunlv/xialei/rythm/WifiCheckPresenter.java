package com.fanyunlv.xialei.rythm;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.fanyunlv.xialei.rythm.utils.DBhelper;

import java.util.List;

/**
 * Created by admin on 2018/8/22.
 */

public class WifiCheckPresenter {
    public static WifiCheckPresenter sWifiCheckPresenter;

    private Context context;
    private WifiManager wifiManager;

    private WifiCheckPresenter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
        String name = wifiManager.getConnectionInfo().getSSID().replaceAll("\"", "");
        if ("<unknown ssid>".equals(name)) {
            return context.getResources().getString(R.string.none_wifi);
        }
        return wifiManager.getConnectionInfo().getSSID().replaceAll("\"","");
    }

    public void handlewifi() {
        List<String> list = DBhelper.getInstance(context).getWifiList();
        for (String item : list) {
            if (getSSIDname().contains(item)) {
                enableWifi(false);
                break;
            }
        }
    }

    public void addNewBlackWifi(String name) {
        DBhelper.getInstance(context).insertwifi(name);
    }

}
