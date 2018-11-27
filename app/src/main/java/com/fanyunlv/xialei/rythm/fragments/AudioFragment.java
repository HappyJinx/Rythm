package com.fanyunlv.xialei.rythm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.RingmodePresenter;

/**
 * Created by xialei on 2018/11/24.
 */
public class AudioFragment extends BaseFragment implements View.OnClickListener{

    private TextView ringmodetext;
    private TextView currenttime;
    private TextView setchangeTime;
    private Button setTimeBttn;

    private RingmodePresenter ringmodePresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_fragment, container, false);
        ringmodetext = (TextView) view.findViewById(R.id.ringmodetext);
        currenttime = (TextView) view.findViewById(R.id.current_time);
        setchangeTime = (TextView) view.findViewById(R.id.set_time);
        setTimeBttn = (Button) view.findViewById(R.id.settime_btn);
        setTimeBttn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringmodePresenter = RingmodePresenter.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTimeString();
    }

    public void updateTimeString() {
        handleTimeChange();
        ringmodetext.setText(ringmodePresenter.getCurrentMode());
        currenttime.setText(ringmodePresenter.getTime());
        setchangeTime.setText(DBhelper.getInstance(getContext()).getSelectedTime());
    }

    public void handleTimeChange() {
        ringmodePresenter.handletime();
    }

    @Override
    public void onClick(View v) {
        getContext().startActivity(new Intent("xialei.action.start.settime"));
    }
}
