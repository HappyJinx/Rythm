package com.fanyunlv.xialei.rythm.location;


/**
 * Created by xialei on 2018/11/27.
 */
public class MyLocation {
    private String name;
//    private Location location;
    private double longti;
    private double lati;
    private double radios;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyLocation(String name, double longti, double lati,double radios) {
        this.name = name;
        this.longti = longti;
        this.lati = lati;
        this.radios = radios;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getRadios() {
        return radios;
    }

    public void setRadios(double radios) {
        this.radios = radios;
    }
}
