package com.fanyunlv.xialei.rythm.presenter;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by xialei on 2018/12/15.
 */
public class PresenterMain {
    private static final String TAG = PresenterMain.class.getSimpleName();
    private static PresenterMain spreMain;

    private Context app;
    private PresenterMain(Context application) {
        app = application;
    }

    public static PresenterMain getInstance(Context application) {
        if (spreMain == null) {
            spreMain = new PresenterMain(application);
        }
        return spreMain;
    }

    public void endableNFC() {
//        try {
//            INfcAdapter mService;
//            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
//            IBinder binder = (IBinder) method.invoke(null, "nfc");
//            if(binder != null) {
//                mService = INfcAdapter.Stub.asInterface(binder);
//            }
//
//            if(mService != null){
//                Log.i(TAG, "onClick true");
//            }

//            Class<?> clz = Class.forName("android.os.ServiceManager");
//            Method getService = clz.getMethod("getService", String.class);
//
//            Object nfcService = getService.invoke(null, Context.NFC_SERVICE);
//
//            Class<?> cStub =  Class.forName("android.nfc.INfcAdapter$Stub");
//            Method asInterface = cStub.getMethod("asInterface", IBinder.class);
//            Object InfcManager = asInterface.invoke(null, nfcService);
//            Method enableme = InfcManager.getClass().getMethod("enable");
//            enableme.invoke(InfcManager);
//            Log.i(TAG, "onClick" + on);

//            Class<?> clz = Class.forName("com.android.nfc.NfcService$EnableDisableTask");
//            Constructor constructor = clz.getConstructors();
//            Object service = constructor.newInstance(app);
//            service.getClass().getMethod()
//            Method method = clz.getDeclaredMethod("enableInternal");
//            if (null != method) {
//                Log.i(TAG, "LineNum:58  Method:endableNFC--> me ="+method);
//            }
//        } catch (Exception e) {
//            Log.i(TAG, "onClick"+on+" e= "+e.toString());
//        }

        Toast.makeText(app,"现在去开启NFC",Toast.LENGTH_SHORT).show();
    }
}
