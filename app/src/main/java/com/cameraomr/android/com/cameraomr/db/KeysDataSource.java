package com.cameraomr.android.com.cameraomr.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cameraomr.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harsha on 9/11/15.
 */
public class KeysDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;
    private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TEMPLATE_ID,
            MySQLiteOpenHelper.COLUMN_TITLE, MySQLiteOpenHelper.COLUMN_DATE, MySQLiteOpenHelper.COLUMN_ANSWERS };

    public KeysDataSource(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Key getKey(String id)
    {
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_KEYS,
                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        if(cursor == null)
            return null;
        cursor.moveToFirst();
        Key key = cursorToKey(cursor);
        cursor.close();
        return key;
    }

    public Key createKey(String template_id, String title, String date, String answers) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteOpenHelper.COLUMN_TEMPLATE_ID, Long.valueOf(template_id));
        values.put(MySQLiteOpenHelper.COLUMN_TITLE, title);
        values.put(MySQLiteOpenHelper.COLUMN_DATE, date);
        values.put(MySQLiteOpenHelper.COLUMN_ANSWERS, answers);
        long insertId = database.insert(MySQLiteOpenHelper.TABLE_KEYS, null, values);
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_KEYS,
                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Key newKey = cursorToKey(cursor);
        cursor.close();
        return newKey;
    }

    public void deleteKeyById(String Id) {
        long id = Long.parseLong(Id);
        System.out.println("Key deleted with id: " + id);
        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteKey(Key key) {
        long id = key.getId();
        System.out.println("Key deleted with id: " + id);
        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Key> getAllKeys() {
        List<Key> keys = new ArrayList<Key>();

        String condition = "date(" + MySQLiteOpenHelper.COLUMN_DATE + ") ASC";
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_KEYS,
                allColumns, null, null, null, null, condition);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Key key = cursorToKey(cursor);
            keys.add(key);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return keys;
    }

    private Key cursorToKey(Cursor cursor) {
        Key key = new Key();
        key.setId(cursor.getLong(0));
        key.setTemplate_id(cursor.getLong(1));
        key.setTitle(cursor.getString(2));
        key.setDate(cursor.getString(3));
        key.setAnswers(cursor.getString(4));
        return key;
    }
}
