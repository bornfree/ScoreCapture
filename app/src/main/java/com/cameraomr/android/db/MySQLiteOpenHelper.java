package com.cameraomr.android.db;

import android.content.ContentValues;
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
    public static final String COLUMN_TEMPLATE_ID = "template_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_ANSWERS = "answers";

    public static final String TABLE_TEMPLATES = "templates";
    public static final String TEMPLATE_COLUMN_ID = "_id";
    public static final String TEMPLATE_COLUMN_HEIGHT = "height";
    public static final String TEMPLATE_COLUMN_WIDTH = "width";
    public static final String TEMPLATE_COLUMN_NUM_ANSWERS = "num_answers";
    public static final String TEMPLATE_COLUMN_NUM_OPTIONS = "num_options";

    public static final String TABLE_SECTIONS = "sections";
    public static final String SECTION_COLUMN_ID = "_id";
    public static final String SECTION_COLUMN_TEMPLATE_ID = "template_id";
    public static final String SECTION_COLUMN_HEIGHT = "height";
    public static final String SECTION_COLUMN_WIDTH = "width";
    public static final String SECTION_COLUMN_TOP = "top";
    public static final String SECTION_COLUMN_LEFT = "left";
    public static final String SECTION_COLUMN_NUM_ANSWERS = "num_answers";


    private static final String DATABASE_NAME = "cameraomr.db";
    private static final int DATABASE_VERSION = 5;

    // Database creation sql statement
    private static final String KEYS_TABLE_CREATE = "create table "
            + TABLE_KEYS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TEMPLATE_ID + " integer not null, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DATE + " text, "
            + COLUMN_ANSWERS + " text not null "
            + ");";

    private static final String TEMPLATES_TABLE_CREATE = "create table "
            + TABLE_TEMPLATES + "("
            + TEMPLATE_COLUMN_ID     + " integer primary key autoincrement, "
            + TEMPLATE_COLUMN_HEIGHT + " integer, "
            + TEMPLATE_COLUMN_WIDTH  + " integer, "
            + TEMPLATE_COLUMN_NUM_ANSWERS  + " integer, "
            + TEMPLATE_COLUMN_NUM_OPTIONS  + " integer "
            + ");";

    private static final String SECTIONS_TABLE_CREATE = "create table "
            + TABLE_SECTIONS + "("
            + SECTION_COLUMN_ID     + " integer primary key autoincrement, "
            + SECTION_COLUMN_TEMPLATE_ID + " integer, "
            + SECTION_COLUMN_HEIGHT  + " integer, "
            + SECTION_COLUMN_WIDTH  + " integer, "
            + SECTION_COLUMN_TOP  + " integer, "
            + SECTION_COLUMN_LEFT  + " integer, "
            + SECTION_COLUMN_NUM_ANSWERS + " integer, "
            + "FOREIGN KEY(" + SECTION_COLUMN_TEMPLATE_ID + ") REFERENCES "
            + TABLE_TEMPLATES + "(" + TEMPLATE_COLUMN_ID + ")"
            + ");";


    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(KEYS_TABLE_CREATE);
        db.execSQL(TEMPLATES_TABLE_CREATE);
        db.execSQL(SECTIONS_TABLE_CREATE);
        setupDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS);
        onCreate(db);
    }

    public void setupDefaultData(SQLiteDatabase db)
    {
        // Insert the base 20 questions, 5 answers template
        ContentValues values = new ContentValues();
        values.put(TEMPLATE_COLUMN_HEIGHT, 640);
        values.put(TEMPLATE_COLUMN_WIDTH, 480);
        values.put(TEMPLATE_COLUMN_NUM_ANSWERS, 20);
        values.put(TEMPLATE_COLUMN_NUM_OPTIONS, 5);
        long tId = db.insert(TABLE_TEMPLATES, null, values);

        // Add section 1
        values.clear();
        values.put(SECTION_COLUMN_TEMPLATE_ID, tId);
        values.put(SECTION_COLUMN_HEIGHT, 432);
        values.put(SECTION_COLUMN_WIDTH, 172);
        values.put(SECTION_COLUMN_TOP, 161);
        values.put(SECTION_COLUMN_LEFT, 56);
        values.put(SECTION_COLUMN_NUM_ANSWERS, 10);
        long sId = db.insert(TABLE_SECTIONS, null, values);

        // Add section 2
        values.clear();
        values.put(SECTION_COLUMN_TEMPLATE_ID, tId);
        values.put(SECTION_COLUMN_HEIGHT, 432);
        values.put(SECTION_COLUMN_WIDTH, 172);
        values.put(SECTION_COLUMN_TOP, 161);
        values.put(SECTION_COLUMN_LEFT, 283);
        values.put(SECTION_COLUMN_NUM_ANSWERS, 10);
        sId = db.insert(TABLE_SECTIONS, null, values);

        // Insert the base 10 questions, 5 answers template
        values.clear();
        values.put(TEMPLATE_COLUMN_HEIGHT, 640);
        values.put(TEMPLATE_COLUMN_WIDTH, 480);
        values.put(TEMPLATE_COLUMN_NUM_ANSWERS, 10);
        values.put(TEMPLATE_COLUMN_NUM_OPTIONS, 5);
        tId = db.insert(TABLE_TEMPLATES, null, values);

        // Add section 1
        values.clear();
        values.put(SECTION_COLUMN_TEMPLATE_ID, tId);
        values.put(SECTION_COLUMN_HEIGHT, 432);
        values.put(SECTION_COLUMN_WIDTH, 172);
        values.put(SECTION_COLUMN_TOP, 161);
        values.put(SECTION_COLUMN_LEFT, 56);
        values.put(SECTION_COLUMN_NUM_ANSWERS, 10);
        sId = db.insert(TABLE_SECTIONS, null, values);
    }
}
