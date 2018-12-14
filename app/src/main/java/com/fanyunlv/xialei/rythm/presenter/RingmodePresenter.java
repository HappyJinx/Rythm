package com.fanyunlv.xialei.rythm.presenter;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.TimeItem;
import com.fanyunlv.xialei.rythm.utils.DBhelper;
import com.fanyunlv.xialei.rythm.utils.DBhelper.OnDBchangedListener;
import com.fanyunlv.xialei.rythm.utils.TaskUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2018/8/22.
 */

public class RingmodePresenter implements OnDBchangedListener{
    private static final String TAG = RingmodePresenter.class.getSimpleName();

    public static RingmodePresenter sringmodePresenter;

    private Context context;
    private AudioManager audioManager;
    private String[] strings;

    private ArrayList<TaskItems> timeTasks;
    private List<TimeItem> timeItemList;
    private DBhelper dBhelper;

    private RingmodePresenter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        strings =  context.getResources().getStringArray(R.array.Ringmodestring);
        dBhelper = DBhelper.getInstance(context);
        dBhelper.addListener(this);
        timeTasks = dBhelper.getTimeTaskList();
        timeItemList = dBhelper.getTimeList();
    }

    public static RingmodePresenter getInstance(Context context) {
        if (sringmodePresenter == null) {
            sringmodePresenter = new RingmodePresenter(context);
        }
        return sringmodePresenter;
    }

    public boolean isNormolMode() {
        if (audioManager != null) {
            return audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
        }
        return true;
    }

    public void handletime() {
        Calendar calendar = Calendar.getInstance();
        for (TimeItem item : timeItemList) {
            if ((calendar.get(Calendar.HOUR_OF_DAY) == item.getHour() && calendar.get(Calendar.MINUTE) == item.getMinute())) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
        }
    }

    public void checkTimeTask() {
        if (timeTasks.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            for (TaskItems taskItems : timeTasks) {
                if (calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE) == taskItems.getCode()) {
                    Log.i(TAG, "LineNum:76  Method:checkTimeTask--> ");
                    TaskUtil.getInstance(context).handleTask(taskItems);
                }
            }
        }
    }

    public void OpenRingmode() {
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    /*
      Reason : set customer mode
      Author : xialei
      Date : 2018/12/14
    */
    public void setAudioMode(int mode) {
        if (audioManager.getRingerMode() != mode) {
            audioManager.setRingerMode(mode);
        }
    }

    public void setVolume(int volume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,0);
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE);
    }

    public String getCurrentMode() {
        if (audioManager != null) {

            return strings[audioManager.getRingerMode()];
        }
        return strings[3];
    }

    @Override
    public void onDBchanged() {
        timeTasks = dBhelper.getTimeTaskList();
        timeItemList = dBhelper.getTimeList();
    }
}
