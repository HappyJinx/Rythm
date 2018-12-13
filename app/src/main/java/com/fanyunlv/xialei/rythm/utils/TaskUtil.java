package com.fanyunlv.xialei.rythm.utils;

import android.content.Context;
import android.util.Log;

import com.fanyunlv.xialei.rythm.beans.TaskItems;
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
        if (task.getAudio() == 1) {
            RingmodePresenter.getInstance(context).OpenRingmode();
        }
        if (task.getWifi() == 1) {
            WifiCheckPresenter.getInstance(context).handlewifi();
        }

    }
}
