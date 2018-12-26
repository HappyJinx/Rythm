package com.fanyunlv.xialei.rythm.utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.presenter.LocationPresenter;
import com.fanyunlv.xialei.rythm.presenter.PresenterMain;
import com.fanyunlv.xialei.rythm.presenter.RingmodePresenter;
import com.fanyunlv.xialei.rythm.presenter.WifiCheckPresenter;

/**
 * Created by xialei on 2018/12/13.
 */
public class TaskUtil implements DBhelper.OnTaskDBchangeListener {
    private static final String TAG = TaskUtil.class.getSimpleName();

    Context context;

    private static TaskUtil taskUtil;

    private TaskUtil(Context context) {
        this.context = context;
        DBhelper.getInstance(context).addtListener(this);
    }

    public static TaskUtil getInstance(Context context) {
        if (taskUtil == null) {
            taskUtil = new TaskUtil(context);
        }
        return taskUtil;
    }


    @Override
    public void onTaskChanged() {
        Log.i(TAG, "LineNum:40  Method:onTaskChanged--> ");
        LocationPresenter.getInstance(context).setLocationMode(LocationPresenter.FAST_MODE);
    }

    public void checkTimeandLocation(BDLocation location) {
        int resultTime = RingmodePresenter.getInstance(context).checkTimeTask();
        int resultLocation = LocationPresenter.getInstance(context).checkDistance(location);

        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:45  Method:checkTimeandLocation--> result 1 =" + resultTime + " -- result 2 =" + resultLocation);
        LocationPresenter.getInstance(context).setLocationMode(Math.min(resultTime, resultLocation));
    }


    public void handleTask(TaskItems task) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:30  Method:handleTask--> task ="+task.toString());
        handAudio(task);
        handWifi(task);
        handVolume(task);
        handNfc(task);
    }

    public void handAudio(TaskItems task) {
        if (task.getAudio() == 1) { //响铃
            RingmodePresenter.getInstance(context).setAudioMode(AudioManager.RINGER_MODE_NORMAL);
        } else if (task.getAudio() == 2) { //震动
            RingmodePresenter.getInstance(context).setAudioMode(AudioManager.RINGER_MODE_VIBRATE);
        } else if (task.getAudio() == 3) { //静音
            RingmodePresenter.getInstance(context).setAudioMode(AudioManager.RINGER_MODE_SILENT);
        }
    }
    public void handWifi(TaskItems task) {
        if (task.getWifi() == 1) { //禁用
            WifiCheckPresenter.getInstance(context).forbidWifi();
        } else if (task.getWifi() == 2) {
            WifiCheckPresenter.getInstance(context).enableWifi(true);
        } else if (task.getWifi() == 3) {
            WifiCheckPresenter.getInstance(context).enableWifi(false);
        }
    }

    public void handVolume(TaskItems task) {
        if (task.getVolume() == 1) {
            RingmodePresenter.getInstance(context).setVolume(8);
        } else if (task.getVolume() == 2) {
            RingmodePresenter.getInstance(context).setVolume(0);
        }
    }

    public void handNfc(TaskItems task) {
        //TODO
        if (task.getNfc() == 1) {
            PresenterMain.getInstance(context).endableNFC(task);
        }
    }

    public int getcode(LatLng location) {
        return (int)((get6Num(location.latitude) + get6Num(location.longitude)) * 1000000);
    }
    public int getcode(BDLocation location) {
        return (int)((get6Num(location.getLatitude()) + get6Num(location.getLongitude())) * 1000000);
    }
    public int getcode(MyLocation location) {
        return (int)((get6Num(location.getLati()) + get6Num(location.getLongti())) * 1000000);
    }
    public double get6Num(Double dd) {
//        Log.i(TAG, "LineNum:107  Method:get6Num--> dd =" + dd);
//        (((int)(dd * 1000000))/1000000)
//        double dc = (double)(((int)(dd * 10000000) / 10) / 1000000);
//        Log.i(TAG, "LineNum:109  Method:get6Num--> dc =" + dc);
//        return dc;
        String result = String.format("%.6f", dd);
        return Double.parseDouble(result);
    }
}
