package com.fanyunlv.xialei.rythm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fanyunlv.xialei.rythm.RythmApplication;

/**
 * Created by xialei on 2018/11/24.
 */
public class SharePrefUtil {
    private static final String TAG = SharePrefUtil.class.getSimpleName();

    public static SharePrefUtil sShareUtil;

    private static final String SHARE_PREF_NAME = "rhythm";

    private SharedPreferences preferences;

    private static final String FIRST_OPEN = "first_open";

    private SharePrefUtil(Context context) {
        preferences = context.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharePrefUtil getInstance(Context context) {
        if (sShareUtil == null) {
            sShareUtil = new SharePrefUtil(context);
        }
        return sShareUtil;
    }

    public boolean isFirstOpen() {
        boolean isopen = preferences.getBoolean(FIRST_OPEN, true);
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "isFirstOpen isopen="+isopen);
        return isopen;
    }

    public void setFirstOpen(boolean firstOpen) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_OPEN, firstOpen);
        editor.commit();
    }

}
