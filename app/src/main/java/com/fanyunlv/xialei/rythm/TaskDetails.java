package com.fanyunlv.xialei.rythm;

/**
 * Created by xialei on 2018/12/8.
 */
public class TaskDetails{
    public int code = 0;
    public String name ;
    public boolean audio = false;
    public boolean wifi = false;
    public boolean volume = false;
    public boolean nfc = false;

    public TaskDetails(int code) {
        this.code = code;
    }

    public TaskDetails(String name, int code, boolean audio, boolean wifi, boolean volume, boolean nfc) {
        this.name = name;
        this.code = code;
        this.audio = audio;
        this.wifi = wifi;
        this.volume = volume;
        this.nfc = nfc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isVolume() {
        return volume;
    }

    public void setVolume(boolean volume) {
        this.volume = volume;
    }

    public boolean isNfc() {
        return nfc;
    }

    public void setNfc(boolean nfc) {
        this.nfc = nfc;
    }
}
