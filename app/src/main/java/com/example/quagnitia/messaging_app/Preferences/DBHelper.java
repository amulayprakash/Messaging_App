package com.example.quagnitia.messaging_app.Preferences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Win on 01-03-2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    // public static final String DATABASE_NAME = "MYLO.db";
    static  final int DATABASE_VERSION = 1;

    public DBHelper(Context context, String DATABASE_NAME)
    {
        super(context, Environment.getExternalStorageDirectory()+"/MASTER.db", null, DATABASE_VERSION);
    }

    public DBHelper(Context context)
    {
        super(context, Environment.getExternalStorageDirectory()+"/ESFAQI/"+"LOG.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Contact Table
        db.execSQL(AlarmLogTable.createAlarmTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Contact Table
        db.execSQL(AlarmLogTable.dropTable());
        onCreate(db);
    }
}
