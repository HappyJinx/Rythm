package com.fanyunlv.xialei.rythm.beans;

/**
 * Created by xialei on 2018/12/8.
 */
public class TimeTaskItem {

    public String name;
    public int enabled;
    public String time="";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        if (enabled > 0) {
            enabled = 1;
        }
        this.enabled = enabled;
    }

    public TimeTaskItem(String name, int enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
