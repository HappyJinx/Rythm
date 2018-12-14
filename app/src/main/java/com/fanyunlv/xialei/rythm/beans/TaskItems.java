package com.fanyunlv.xialei.rythm.beans;

/**
 * Created by xialei on 2018/12/8.
 */
public class TaskItems {

    public String name ;
    public int code = 0;
    public int audio = 0;
    public int wifi = 0;
    public int volume = 0;
    public int nfc = 0;

    public TaskItems(int code) {
        this.code = code;
    }

    public TaskItems(String name, int code, int audio, int wifi, int volume, int nfc) {
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

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getNfc() {
        return nfc;
    }

    public void setNfc(int nfc) {
        this.nfc = nfc;
    }
}
