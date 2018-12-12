package com.fanyunlv.xialei.rythm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fanyunlv.xialei.rythm.TaskDetails;
import com.fanyunlv.xialei.rythm.location.MyLocation;
import com.fanyunlv.xialei.rythm.TimeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class DBhelper {
    private static final String TAG = DBhelper.class.getSimpleName();

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

    public void inserttaskDetails(TaskDetails details) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(RythmDatabase.TASK.NAME,details.getName());
        contentValues.put(RythmDatabase.TASK.CODE,details.getCode());
        contentValues.put(RythmDatabase.TASK.AUDIO,details.isAudio()?1:0);
        contentValues.put(RythmDatabase.TASK.WIFI,details.isWifi()?1:0);
        contentValues.put(RythmDatabase.TASK.VOLUME,details.isVolume()?1:0);
        contentValues.put(RythmDatabase.TASK.NFC,details.isNfc()?1:0);
        db.insert(RythmDatabase.Tables.TASK,null,contentValues);

    }

    public void insertwifi(String name) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.WifiColumes.NAME,name);
        insertwifi(values);
    }

    public void insertwifi(ContentValues values) {
        db.insert(RythmDatabase.Tables.WIFITABLE, null, values);
    }

    public void insertLocation(MyLocation myLocation) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.LOCATIONTABLE.NAME,myLocation.getName());
        values.put(RythmDatabase.LOCATIONTABLE.LONGT,myLocation.getLongti());
        values.put(RythmDatabase.LOCATIONTABLE.LATI,myLocation.getLati());
        values.put(RythmDatabase.LOCATIONTABLE.RADIOUS,myLocation.getRadios());
        values.put(RythmDatabase.LOCATIONTABLE.DESCRIB,myLocation.getDescription());
        insertLocation(values);
    }
    public void insertLocation(ContentValues values) {
        Log.i(TAG, "LineNum:84  Method:insertLocation--> ");
        db.insert(RythmDatabase.Tables.LOCATIONTABLE, null, values);
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

    public void deletelocation(MyLocation myLocation) {
        Log.i("deletelocation", "deletelocation ");
        if (myLocation == null) {
            db.delete(RythmDatabase.Tables.LOCATIONTABLE,null,null);
            return;
        }
        db.delete(RythmDatabase.Tables.LOCATIONTABLE,
                RythmDatabase.LOCATIONTABLE.NAME+"= ? and "+RythmDatabase.LOCATIONTABLE.LONGT+"= ? ",
                new String[]{myLocation.getName(),Double.toString(myLocation.getLongti())});
    }

    public void deletetask(int code) {
        db.delete(RythmDatabase.Tables.TASK,
                RythmDatabase.TASK.CODE+"= ? ",
                new String[]{Integer.toString(code)});
    }

    public void deletelocation(int ID) {
        Log.i("deletelocation", "deletelocation ID"+ID);

        db.delete(RythmDatabase.Tables.LOCATIONTABLE,
                RythmDatabase.LOCATIONTABLE._ID+"= ? ",
                new String[]{Integer.toString(ID)});
//        db.execSQL("delete from location where id="+ID);
    }

    public void deleteitem(String tablename,int postion) {
        db.delete(tablename, RythmDatabase.TimeColumes._ID+"= ?", new String[]{Integer.toString(postion)});
    }

    public void deletetask(int postion) {
        db.delete(RythmDatabase.Tables.TASK, RythmDatabase.TASK._ID+"= ? ", new String[]{Integer.toString(postion)});
    }
    public void deletetask(String tablename,int postion) {
        db.delete(tablename, RythmDatabase.TASK._ID+"= ? ", new String[]{Integer.toString(postion)});
    }

    public Cursor querytime() {
        return db.query(RythmDatabase.Tables.TIMETABLE, null, null,null,null,null,null);
    }
    public Cursor querywifi() {
        return db.query(RythmDatabase.Tables.WIFITABLE, null, null,null,null,null,null);
    }
    public Cursor querylocation() {
        return db.query(RythmDatabase.Tables.LOCATIONTABLE, null, null,null,null,null,null);
    }
    public Cursor querytask(int code) {
        return db.query(RythmDatabase.Tables.TASK, null, RythmDatabase.TASK.CODE+"= ?",new String[]{Integer.toString(code)},null,null,null);
    }
    public Cursor querytask(String name) {
        return db.query(RythmDatabase.Tables.TASK, null, RythmDatabase.TASK.NAME+"= ?",new String[]{name},null,null,null);
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

    public ArrayList<MyLocation> getLocationList() {
        ArrayList<MyLocation> list = new ArrayList<>();
        db = database.getWritableDatabase();
        //Cursor cursor = db.query(RythmDatabase.Tables.LOCATIONTABLE, null, null, null, null,null, null);
        Cursor cursor = querylocation();
        Log.i(TAG, "LineNum:198  Method:getLocationList--> cusor ="+cursor.getCount());
        while (cursor.moveToNext()) {
            list.add(new MyLocation(cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getDouble(4),cursor.getString(5)));
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

    public ArrayList<TaskDetails> getTaskList() {
        ArrayList<TaskDetails> list = new ArrayList<>();
        db = database.getWritableDatabase();
        //Cursor cursor = db.query(RythmDatabase.Tables.LOCATIONTABLE, null, null, null, null,null, null);
        Cursor cursor = querylocation();
        Log.i(TAG, "LineNum:198  Method:getLocationList--> cusor ="+cursor.getCount());
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            int code = cursor.getInt(1);
            boolean audio = (cursor.getInt(2)==1);
            boolean wifi = (cursor.getInt(3)==1);
            boolean volume = (cursor.getInt(4)==1);
            boolean nfc = (cursor.getInt(5)==1);
            list.add(new TaskDetails(name,code,audio,wifi,volume,nfc));
        }
        cursor.close();
        return list;
    }
}
