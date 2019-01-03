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

    public static final int FAST_MODE = 5;
    public static final int NORMAL_MODE = 15;
    public static final int SLOW_MODE = 3*60;

    public static final int FAST_TIME_THRESHOLD = 3;  //minute
    public static final int NORMAL_TIME_THRESHOLD = 10;

    public static final double FAST_LOCATE_THRESHOLD = 150.00;  //50M
    public static final double FAR_LOCATE_THRESHOLD = 500.00; //500M

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
        LocationPresenter.getInstance(context).setLocationMode(FAST_MODE);
    }

    public void checkTimeandLocation(BDLocation location) {

        int minTime = RingmodePresenter.getInstance(context).checkTimeTask();
        double minDistance = LocationPresenter.getInstance(context).checkDistance(location);

        int howclose = howClose(minTime);
        int howfar = howFar(minDistance);

        int mode = SLOW_MODE;

        switch (Math.min(howclose, howfar)) {
            case -1:
                // no
                break;
            case 0:
                mode = FAST_MODE;
                break;
            case 1:
                mode = NORMAL_MODE;
                break;
            case 2:
                mode = SLOW_MODE;
                break;
        }

        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "checkTimeandLocation--> mode = "+mode+"  minTime = " + minTime + "mins -- minDistance = " + minDistance+"m");
        LocationPresenter.getInstance(context).setLocationMode(mode);
    }

    public int howFar(double mindis) {
        int result = -1;

        if (mindis > FAST_LOCATE_THRESHOLD && mindis < FAR_LOCATE_THRESHOLD) {
            result = 1;                             // 距离一般
        }else {
            result = 2;
        }
        return result;
    }

    public int howClose(int minTime) {
        int result = -1;
        if (minTime <= FAST_TIME_THRESHOLD) {           //  X<3
            result = 0;
        } else if (minTime >= FAST_TIME_THRESHOLD
                && minTime < NORMAL_TIME_THRESHOLD){    //  3<X<10
            result = 1;
        } else if (minTime >= NORMAL_TIME_THRESHOLD) {  //  X>10
            result = 2;
        }
        return result;
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
