package com.fanyunlv.xialei.rythm;

import android.content.Context;
import android.media.AudioManager;

import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2018/8/22.
 */

public class RingmodePresenter {

    public static RingmodePresenter sringmodePresenter;

    private Context context;
    private AudioManager audioManager;
    private String[] strings;

    private RingmodePresenter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        strings =  context.getResources().getStringArray(R.array.Ringmodestring);
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
        List<TimeItem> timeItemList = DBhelper.getInstance(context).getTimeList();
        for (TimeItem item : timeItemList) {
            if ((calendar.get(Calendar.HOUR_OF_DAY) == item.getHour() && calendar.get(Calendar.MINUTE) == item.getMinute())) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
        }
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
}
