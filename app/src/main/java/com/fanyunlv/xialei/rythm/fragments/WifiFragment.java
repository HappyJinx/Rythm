package com.fanyunlv.xialei.rythm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.DBhelper;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.WifiCheckPresenter;

/**
 * Created by xialei on 2018/11/24.
 */
public class WifiFragment extends BaseFragment implements View.OnClickListener{

    private WifiCheckPresenter wifiCheckPresenter;

    private TextView wifinametext;
    private TextView black_wifi;
    private Button setwifi_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wifi_fragment, container, false);
        wifinametext = (TextView) view.findViewById(R.id.wifinametext);
        black_wifi = (TextView) view.findViewById(R.id.black_wifi);
        setwifi_btn = (Button) view.findViewById(R.id.setwifi_btn);
        setwifi_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiCheckPresenter = WifiCheckPresenter.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWifiString();
    }

    private void updateWifiString() {
        wifiCheckPresenter.handlewifi();
        wifinametext.setText(wifiCheckPresenter.getSSIDname());
        black_wifi.setText(DBhelper.getInstance(getContext()).getSelectedWifi());
    }

    @Override
    public void onClick(View v) {
        getContext().startActivity(new Intent("xialei.action.start.setwifi"));
    }
}
