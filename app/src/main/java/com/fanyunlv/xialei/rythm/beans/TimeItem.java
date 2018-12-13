package com.fanyunlv.xialei.rythm.beans;

/**
 * Created by admin on 2018/8/24.
 */

public class TimeItem {
    private int hour;
    private int minute;

    public TimeItem(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return hour+":"+minute;
    }
}
