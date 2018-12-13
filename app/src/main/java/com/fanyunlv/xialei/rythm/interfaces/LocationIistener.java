package com.fanyunlv.xialei.rythm.interfaces;

import com.baidu.location.BDLocation;

/**
 * Created by xialei on 2018/12/7.
 */
public interface LocationIistener {
    public void onLocationReceived(BDLocation location);
    public void onLocationNeedNotify(BDLocation bdLocation, float v);
}
