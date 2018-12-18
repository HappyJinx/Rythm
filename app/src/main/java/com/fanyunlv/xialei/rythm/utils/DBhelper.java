package com.fanyunlv.xialei.rythm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.MyLocation;
import com.fanyunlv.xialei.rythm.beans.TaskStateItem;
import com.fanyunlv.xialei.rythm.beans.TimeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class DBhelper {

    private static final String TAG = DBhelper.class.getSimpleName();

    public interface OnDBchangedListener {
        public void onDBchanged();
    };

    public static DBhelper dBhelper;
    private Context context;
    private RythmDatabase database;
    private SQLiteDatabase db;

    private List<OnDBchangedListener> listeners;

    private DBhelper(Context context){
        this.context = context;
        database = RythmDatabase.getInstance(context);
        db = database.getWritableDatabase();
        listeners = new ArrayList<>();
    }

    public static DBhelper getInstance(Context context) {
        if (dBhelper == null) {
            dBhelper = new DBhelper(context);
        }
        return dBhelper;
    }

    public void addListener(OnDBchangedListener listener) {
        listeners.add(listener);
    }

    public void insertdb(int hour,int minute) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.TimeColumes.HOUR,hour);
        values.put(RythmDatabase.TimeColumes.MINUTE,minute);
        insertdb(values);
        notifyListenr();
    }

    public void insertdb(ContentValues values) {
        db.insert(RythmDatabase.Tables.TIMETABLE, null, values);
        notifyListenr();
    }

    public void inserttaskDetails(TaskItems details) {
        Log.i(TAG, "LineNum:69  Method:inserttaskDetails--> "+details.getCode());
        ContentValues contentValues = new ContentValues();
        contentValues.put(RythmDatabase.TASK.NAME,details.getName());
        contentValues.put(RythmDatabase.TASK.CODE,details.getCode());
        contentValues.put(RythmDatabase.TASK.AUDIO,details.getAudio());
        contentValues.put(RythmDatabase.TASK.WIFI,details.getWifi());
        contentValues.put(RythmDatabase.TASK.VOLUME,details.getVolume());
        contentValues.put(RythmDatabase.TASK.NFC,details.getNfc());
        db.insert(RythmDatabase.Tables.TASK,null,contentValues);

        notifyListenr();
    }
    public void updatetaskDetails(TaskItems details) {
        Log.i(TAG, "LineNum:82  Method:updatetaskDetails--> "+details.getCode());
        ContentValues contentValues = new ContentValues();
        contentValues.put(RythmDatabase.TASK.AUDIO,details.getAudio());
        contentValues.put(RythmDatabase.TASK.WIFI,details.getWifi());
        contentValues.put(RythmDatabase.TASK.VOLUME,details.getVolume());
        contentValues.put(RythmDatabase.TASK.NFC,details.getNfc());
        db.update(RythmDatabase.Tables.TASK,contentValues,RythmDatabase.TASK.CODE+"= ?",new String[]{Integer.toString(details.getCode())});

        notifyListenr();
    }

    public void notifyListenr() {
        if (listeners.size() > 0) {
            for (OnDBchangedListener listener : listeners) {
                listener.onDBchanged();
            }
        }
    }

    public void insertwifi(String name) {
        ContentValues values = new ContentValues();
        values.put(RythmDatabase.WifiColumes.NAME,name);
        insertwifi(values);
        notifyListenr();
    }

    public void insertwifi(ContentValues values) {
        db.insert(RythmDatabase.Tables.WIFITABLE, null, values);
        notifyListenr();
    }


    public void insertLocation(ContentValues values) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "LineNum:117  Method:insertLocation--> ");
        db.insert(RythmDatabase.Tables.LOCATIONTABLE, null, values);
        notifyListenr();
    }

    public void insertdb(String tablename, ContentValues values) {
        db.insert(tablename, null, values);
        notifyListenr();
    }
    public void deleteitem(int hour,int minute) {
        db.delete(RythmDatabase.Tables.TIMETABLE,
                RythmDatabase.TimeColumes.HOUR+"= ? and "+RythmDatabase.TimeColumes.MINUTE+"= ? ",
                new String[]{Integer.toString(hour),Integer.toString(minute)});
        notifyListenr();
    }

    public void deletewifi(String name) {
        db.delete(RythmDatabase.Tables.WIFITABLE,
                RythmDatabase.WifiColumes.NAME+"= ? ",
                new String[]{name});
        notifyListenr();
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
        notifyListenr();
    }

    public void deletetask(int code) {
        db.delete(RythmDatabase.Tables.TASK,
                RythmDatabase.TASK.CODE+"= ? ",
                new String[]{Integer.toString(code)});
        notifyListenr();
    }

    public void deletelocation(int ID) {
        Log.i("deletelocation", "deletelocation ID"+ID);

        db.delete(RythmDatabase.Tables.LOCATIONTABLE,
                RythmDatabase.LOCATIONTABLE._ID+"= ? ",
                new String[]{Integer.toString(ID)});
//        db.execSQL("delete from location where id="+ID);
        notifyListenr();
    }

    public void deleteitem(String tablename,int postion) {
        db.delete(tablename, RythmDatabase.TimeColumes._ID+"= ?", new String[]{Integer.toString(postion)});
        notifyListenr();
    }

    public void deletetask(String tablename,int postion) {
        db.delete(tablename, RythmDatabase.TASK._ID+"= ? ", new String[]{Integer.toString(postion)});
        notifyListenr();
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
    public Cursor querylocation(int code ) {
        return db.query(RythmDatabase.Tables.LOCATIONTABLE, null, RythmDatabase.LOCATIONTABLE.CODE+"= ?",new String[]{Integer.toString(code)},null,null,null);
    }
    public Cursor querytask(int code) {
        return db.query(RythmDatabase.Tables.TASK, null, RythmDatabase.TASK.CODE+"= ?",new String[]{Integer.toString(code)},null,null,null);
    }
    public Cursor querytask() {
        return db.query(RythmDatabase.Tables.TASK, null, null,null,null,null,null);
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
        while (cursor.moveToNext()) {
            list.add(new MyLocation(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    cursor.getDouble(5),
                    cursor.getString(6)
            ));
        }
        cursor.close();
        return list;
    }

    public MyLocation getLocation(int code) {
        Cursor cursor = querylocation(code);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        MyLocation myLocation = new MyLocation(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getDouble(3),
                cursor.getDouble(4),
                cursor.getDouble(5),
                cursor.getString(6)
        );
        cursor.close();
        return myLocation;
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

    public ArrayList<TaskItems> getLocatiosTaskList() {
        ArrayList<TaskItems> list = new ArrayList<>();
        db = database.getWritableDatabase();
        Cursor cursor = querytask();
        while (cursor.moveToNext()) {
            Log.i(TAG, "LineNum:298  Method:getLocatiosTaskList--> cursor.getInt(2)="+cursor.getInt(2));
            if (cursor.getInt(2) > 10000) {
                list.add(new TaskItems(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                        ));
            }
        }
        cursor.close();
        return list;
    }

    public ArrayList<TaskItems> getTimeTaskList() {
        ArrayList<TaskItems> list = new ArrayList<>();
        db = database.getWritableDatabase();
        Cursor cursor = querytask();
        while (cursor.moveToNext()) {
            if (cursor.getInt(2) < 10000) {
                list.add(new TaskItems(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
            }
        }
        cursor.close();
        return list;
    }

    public TaskItems getTask(int code) {
        TaskItems taskItems = null;
        db = database.getWritableDatabase();
        Cursor cursor = querytask(code);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            taskItems = new TaskItems(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6)
                );
        }
        cursor.close();
        return taskItems;
    }

}
