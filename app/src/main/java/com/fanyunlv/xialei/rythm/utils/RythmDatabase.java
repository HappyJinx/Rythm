package com.fanyunlv.xialei.rythm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fanyunlv.xialei.rythm.RythmApplication;
import com.fanyunlv.xialei.rythm.beans.TaskItems;
import com.fanyunlv.xialei.rythm.beans.MyLocation;

/**
 * Created by admin on 2018/8/24.
 */

public class RythmDatabase extends SQLiteOpenHelper {

    private final String TAG = "Rythm";
    private static final String db_name = "Rythm";
    private static final int dbversion = 1;

    public static RythmDatabase database;
    private SQLiteDatabase mdb;

    public interface Tables{
        static final String TIMETABLE = "time";
        static final String WIFITABLE = "wifi";
        static final String LOCATIONTABLE = "location";
        static final String TASK = "task";
    }
    public interface TimeColumes{
        static final String _ID = "id";
        static final String HOUR = "hour";
        static final String MINUTE = "minute";
    }
    public interface WifiColumes{
        static final String _ID = "id";
        static final String NAME = "name";
    }
    public interface LOCATIONTABLE{
        static final String _ID = "id";
        static final String NAME = "name";
        static final String CODE = "code";
        static final String LONGT = "longt";
        static final String LATI = "lati";
        static final String RADIOUS = "radios";
        static final String DESCRIB = "describ";
    }

    public interface TASK{
        static final String _ID = "id";
        static final String CODE = "code";
        static final String NAME = "name";
        static final String AUDIO = "audio";
        static final String WIFI = "wifi";
        static final String VOLUME = "volume";
        static final String NFC = "nfc";
    }

    public static RythmDatabase getInstance(Context context) {
        if (database == null) {
            database = new RythmDatabase(context);
        }
        return database;
    }

    private RythmDatabase(Context context){
        super(context,db_name,null,dbversion);
    }

    public RythmDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onCreate: database");
        dropTables(db);
        String sql = "create table "+Tables.TIMETABLE+"("+
                TimeColumes._ID+" integer primary key autoincrement,"+
                TimeColumes.HOUR+" integer ," +
                TimeColumes.MINUTE+" integer)";

        db.execSQL(sql);

        String sql2 = "create table "+Tables.WIFITABLE+"("+
                WifiColumes._ID+" integer primary key autoincrement,"+
                WifiColumes.NAME+" text)";

        db.execSQL(sql2);

        String sql3 = "create table "+Tables.LOCATIONTABLE+"("+
                LOCATIONTABLE._ID+" integer primary key autoincrement,"+
                LOCATIONTABLE.NAME+" text ,"+
                LOCATIONTABLE.CODE+" integer ,"+
                LOCATIONTABLE.LONGT+" real ,"+
                LOCATIONTABLE.LATI+" real ,"+
                LOCATIONTABLE.RADIOUS+" real ,"+
                LOCATIONTABLE.DESCRIB+" text)";

        db.execSQL(sql3);

        String sql4 = "create table "+Tables.TASK+"("+
                TASK._ID+" integer primary key autoincrement,"+
                TASK.NAME+" text ,"+
                TASK.CODE+" integer unique,"+
                TASK.AUDIO+" integer ,"+
                TASK.WIFI+" integer ,"+
                TASK.VOLUME+" integer ,"+
                TASK.NFC+" integer)";

        db.execSQL(sql4);

        mdb = db;

        insertItem(11,50);
        insertItem(17,50);
        insetTimeTask(1150);
        insetTimeTask(1750);
        insertWifi("Rainbow");
    }

    private void insertItem(int hour, int minute) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(RythmDatabase.TimeColumes.HOUR,hour);
        contentValues.put(RythmDatabase.TimeColumes.MINUTE,minute);
        mdb.insert(Tables.TIMETABLE,null,contentValues);
    }

    private void insetTimeTask(int code) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK.NAME,"");
        contentValues.put(TASK.CODE,code);
        contentValues.put(TASK.AUDIO,1);
        contentValues.put(TASK.WIFI,0);
        contentValues.put(TASK.VOLUME,0);
        contentValues.put(TASK.NFC,0);
        mdb.insert(Tables.TASK,null,contentValues);
    }

    private void insertWifi(String name) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(WifiColumes.NAME,name);
        mdb.insert(Tables.WIFITABLE,null,contentValues);
    }

    public void dropTables(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + Tables.TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TIMETABLE+";");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.WIFITABLE+";");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.LOCATIONTABLE+";");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TASK+";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (RythmApplication.ENABLE_LOG)Log.i(TAG, "onUpgrade: database");
    }

}
