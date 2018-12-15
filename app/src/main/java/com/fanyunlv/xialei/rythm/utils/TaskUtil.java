package com.fanyunlv.xialei.rythm.utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.presenter.PresenterMain;
import com.fanyunlv.xialei.rythm.presenter.RingmodePresenter;
import com.fanyunlv.xialei.rythm.presenter.WifiCheckPresenter;

/**
 * Created by xialei on 2018/12/13.
 */
public class TaskUtil {
    private static final String TAG = TaskUtil.class.getSimpleName();

    Context context;

    private static TaskUtil taskUtil;

    private TaskUtil(Context context) {
        this.context = context;
    }

    public static TaskUtil getInstance(Context context) {
        if (taskUtil == null) {
            taskUtil = new TaskUtil(context);
        }
        return taskUtil;
    }

    public void handleTask(TaskItems task) {
        Log.i(TAG, "LineNum:30  Method:handleTask--> ");
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
            PresenterMain.getInstance(context).endableNFC();
        }
    }
}
