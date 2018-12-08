package com.fanyunlv.xialei.rythm;

/**
 * Created by xialei on 2018/12/8.
 */
public class TimeTaskItem {
    public String name;
    public boolean enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TimeTaskItem(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
