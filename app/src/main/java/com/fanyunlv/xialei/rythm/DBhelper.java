package com.fanyunlv.xialei.rythm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class DBhelper {

    public static DBhelper dBhelper;
    private Context context;
    private RythmDatabase database;
    private SQLiteDatabase db;

    private DBhelper(Context context){
        this.context = context;
        database = RythmDatabase.getInstance(context);
        db = database.getWritableDatabase();
    }

    public static DBhelper getInstance(Context context) {
        if (dBhelper == null) {
            dBhelper = new DBhelper(context);
        }
        return dBhelper;
    }

    public void insertdb(int hour,int minute) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.TimeColumes.HOUR,hour);
        values.put(RythmDatabase.TimeColumes.MINUTE,minute);
        insertdb(values);
    }

    public void insertdb(ContentValues values) {
        db.insert(RythmDatabase.Tables.TIMETABLE, null, values);
    }

    public void insertwifi(String name) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.WifiColumes.NAME,name);
        insertwifi(values);
    }

    public void insertwifi(ContentValues values) {
        db.insert(RythmDatabase.Tables.WIFITABLE, null, values);
    }

    public void insertdb(String tablename, ContentValues values) {
        db.insert(tablename, null, values);
    }
    public void deleteitem(int hour,int minute) {
        db.delete(RythmDatabase.Tables.TIMETABLE,
                RythmDatabase.TimeColumes.HOUR+"= ? and "+RythmDatabase.TimeColumes.MINUTE+"= ? ",
                new String[]{Integer.toString(hour),Integer.toString(minute)});
    }

    public void deletewifi(String name) {
        db.delete(RythmDatabase.Tables.WIFITABLE,
                RythmDatabase.WifiColumes.NAME+"= ? ",
                new String[]{name});
    }


    public void deleteitem(String tablename,int postion) {
        db.delete(tablename, RythmDatabase.TimeColumes._ID+"=? and", new String[]{Integer.toString(postion)});
    }

    public Cursor querytime() {
        return db.query(RythmDatabase.Tables.TIMETABLE, null, null,null,null,null,null);
    }
    public Cursor querywifi() {
        return db.query(RythmDatabase.Tables.WIFITABLE, null, null,null,null,null,null);
    }

    public String getSelectedTime() {
        Cursor cursor = querytime();
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            stringBuffer.append(cursor.getInt(1));
            stringBuffer.append(":");
            stringBuffer.append(cursor.getInt(2));
            stringBuffer.append("  ");
        }
        cursor.close();
        return stringBuffer.toString();
    }

    public String getSelectedWifi() {
        Cursor cursor = querywifi();
        if (cursor == null) {
            return "";
        }
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String name = cursor.getString(1);
            cursor.close();
            return name;
        }else {
            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()) {
                stringBuffer.append(cursor.getString(1));
                stringBuffer.append("\n");

            }
            cursor.close();
            return stringBuffer.toString();

        }

    }
    public List<String> getWifiList() {
        ArrayList<String> list = new ArrayList<>();
        db = database.getWritableDatabase();
        Cursor cursor = db.query(RythmDatabase.Tables.WIFITABLE, null, null, null, null,null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }
        cursor.close();
        return list;
    }

    public List<TimeItem> getTimeList() {
        ArrayList<TimeItem> list = new ArrayList<>();
        db = database.getWritableDatabase();
        Cursor cursor = db.query(RythmDatabase.Tables.TIMETABLE, null, null, null, null,null, null);
        while (cursor.moveToNext()) {
            list.add(new TimeItem(cursor.getInt(1) ,cursor.getInt(2)));
        }
        cursor.close();
        return list;
    }
}
