package com.example.quagnitia.messaging_app.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by nikita on 21-05-2018.
 */

public class AlarmLogTable {

    Context context;
    static DBHelper dbHelper;
    public static final String TABLE_NAME = "LOG_TABLE";
    public static final String COL_ID = "id";
    public static final String COL_LOG_TYPE = "log_type";
    public static final String COL_CURRENTTIME = "detail";

    public AlarmLogTable(Context context, DBHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public static String createAlarmTable() {
        String createTableQuery = "create table  If Not Exists " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_LOG_TYPE + " VARCHAR(100)," + COL_CURRENTTIME + " VARCHAR(200));";
        return createTableQuery;
    }

    public static String dropTable() {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return dropTableQuery;
    }

    
   /* public static void deleteContactData() {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
    }*/

    public static Boolean insertLogData(String log_type, String current_time) {
        boolean flag;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_LOG_TYPE, log_type);
        cv.put(COL_CURRENTTIME, current_time);

        long rowid = db.insert(TABLE_NAME, null, cv);

        if (rowid == -1) {
            flag = false;
        } else {
            flag = true;
        }

        return flag;
    }


    public static boolean deleteLogRecords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                db.execSQL("delete from " + TABLE_NAME);
            } while (c.moveToNext());
        }

        return true;
    }


    public static ArrayList<String> fetchAllLogs() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "Select * from " + TABLE_NAME + ";";
        Cursor c = db.rawQuery(query, null);
        ArrayList<String> str_arr = new ArrayList<String>();

        if (c.moveToFirst()) {
            do {
                String str ="";
                str = str + " id : "+c.getString(c.getColumnIndex(COL_ID));
                str = str +" LOG_TYPE : "+c.getString(c.getColumnIndex(COL_LOG_TYPE));
                str = str +" CURRENTTIME : "+c.getString(c.getColumnIndex(COL_CURRENTTIME));
                str_arr.add(str);
            } while (c.moveToNext());
        }

        return str_arr;
    }

}
