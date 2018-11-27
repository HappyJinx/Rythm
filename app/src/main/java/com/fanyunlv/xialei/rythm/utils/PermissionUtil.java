package com.fanyunlv.xialei.rythm.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.Arrays;

/**
 * Created by xialei on 2018/11/27.
 */
public class PermissionUtil {
    private static final String[] LOCATIONS = {
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    private static PermissionUtil spermutil;
    private Activity mcontext;

    private PermissionUtil(Activity context) {
        mcontext = context;
    }

    public static PermissionUtil getInstance(Activity context) {
        if (spermutil == null) {
            spermutil = new PermissionUtil(context);
        }
        return spermutil;
    }

    public boolean checkHaspermissions() {
        for (String permiss : Arrays.asList(LOCATIONS)) {
            if (mcontext.checkSelfPermission(permiss) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public boolean checkHaspermission(String permiss){
        if (mcontext.checkSelfPermission(permiss) != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }
    }

    public void requestLocationPermissions(int requestcode) {
        mcontext.requestPermissions(LOCATIONS,requestcode);
    }

}
