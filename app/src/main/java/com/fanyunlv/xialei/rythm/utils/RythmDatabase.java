package com.fanyunlv.xialei.rythm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fanyunlv.xialei.rythm.TaskDetails;
import com.fanyunlv.xialei.rythm.location.MyLocation;

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
        static final String LONGT = "longt";
        static final String LATI = "lati";
        static final String RADIOUS = "radios";
    }

    public interface TASK{
        static final String _ID = "id";
        static final String CODE = "code";
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
        Log.i(TAG, "onCreate: database");
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
                LOCATIONTABLE.LONGT+" real ,"+
                LOCATIONTABLE.LATI+" real ,"+
                LOCATIONTABLE.RADIOUS+" real)";

        db.execSQL(sql3);

        String sql4 = "create table "+Tables.TASK+"("+
                TASK._ID+" integer primary key autoincrement,"+
                TASK.CODE+" integer ,"+
                TASK.AUDIO+" integer ,"+
                TASK.WIFI+" integer ,"+
                TASK.VOLUME+" integer ,"+
                TASK.NFC+" integer)";

        db.execSQL(sql4);

        mdb = db;

        insertWifi("Rainbow");
    }

    private void insertItem(int hour, int minute) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(RythmDatabase.TimeColumes.HOUR,hour);
        contentValues.put(RythmDatabase.TimeColumes.MINUTE,minute);
        mdb.insert(Tables.TIMETABLE,null,contentValues);
    }

    private void insertWifi(String name) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(WifiColumes.NAME,name);
        mdb.insert(Tables.WIFITABLE,null,contentValues);
    }

    private void insertLocation(MyLocation myLocation) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATIONTABLE.NAME,myLocation.getName());
        contentValues.put(LOCATIONTABLE.LONGT,myLocation.getLongti());
        contentValues.put(LOCATIONTABLE.LATI,myLocation.getLati());
        contentValues.put(LOCATIONTABLE.RADIOUS,myLocation.getLati());
        mdb.insert(Tables.LOCATIONTABLE,null,contentValues);
    }
    private void insertLocation(TaskDetails taskDetails) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK.CODE,taskDetails.getCode());
        contentValues.put(TASK.AUDIO,taskDetails.isAudio()?1:0);
        contentValues.put(TASK.WIFI,taskDetails.isWifi()?1:0);
        contentValues.put(TASK.VOLUME,taskDetails.isVolume()?1:0);
        contentValues.put(TASK.NFC,taskDetails.isNfc()?1:0);
        mdb.insert(Tables.TASK,null,contentValues);
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
        Log.i(TAG, "onUpgrade: database");
    }



}
