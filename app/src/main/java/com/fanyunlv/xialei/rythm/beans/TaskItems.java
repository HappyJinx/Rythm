package com.fanyunlv.xialei.rythm.beans;

/**
 * Created by xialei on 2018/12/8.
 */
public class TaskItems {

    public String name ;
    public int timecode = 0;
    public int audio = 0;
    public int wifi = 0;
    public int volume = 0;
    public int nfc = 0;

    public TaskItems(int timecode) {
        this.timecode = timecode;
    }

    public TaskItems(String name, int timecode, int audio, int wifi, int volume, int nfc) {
        this.name = name;
        this.timecode = timecode;
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

    public int getTimecode() {
        return timecode;
    }

    public void setTimecode(int timecode) {
        this.timecode = timecode;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        if (audio > 0) {
            audio = 1;
        }
        this.audio = audio;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        if (wifi > 0) {
            wifi = 1;
        }
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
        if (nfc > 0) {
            nfc = 1;
        }
        this.nfc = nfc;
    }
}