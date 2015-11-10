package com.cameraomr.android.com.cameraomr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by harsha on 9/11/15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_KEYS = "keys";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_ANSWERS = "answers";


    private static final String DATABASE_NAME = "cameraomr.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String KEYS_TABLE_CREATE = "create table "
            + TABLE_KEYS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_DATE + " text, "
            + COLUMN_ANSWERS + " text not null "
            + ");";


    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KEYS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEYS);
        onCreate(db);
    }
}
