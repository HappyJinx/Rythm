package com.fanyunlv.xialei.rythm.beans;

/**
 * Created by xialei on 2018/12/8.
 */
public class TaskStateItem {

    public String name;
    public int state;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TaskStateItem(String name, int state, String time) {
        this.name = name;
        this.state = state;
        this.time = time;
    }
    public TaskStateItem(String name, int state) {
        this.name = name;
        this.state = state;
    }
}
