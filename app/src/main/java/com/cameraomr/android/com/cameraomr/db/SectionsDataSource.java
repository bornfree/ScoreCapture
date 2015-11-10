/**
 * Created by harsha on 10/11/15.
 */
package com.cameraomr.android.com.cameraomr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SectionsDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;
    private String[] allColumns = { MySQLiteOpenHelper.SECTION_COLUMN_ID, MySQLiteOpenHelper.SECTION_COLUMN_TEMPLATE_ID,
            MySQLiteOpenHelper.SECTION_COLUMN_HEIGHT, MySQLiteOpenHelper.SECTION_COLUMN_WIDTH,
            MySQLiteOpenHelper.SECTION_COLUMN_TOP,  MySQLiteOpenHelper.SECTION_COLUMN_LEFT,
            MySQLiteOpenHelper.SECTION_COLUMN_NUM_ANSWERS};

    public SectionsDataSource(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Section getSection(String id)
    {
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_SECTIONS,
                allColumns, MySQLiteOpenHelper.SECTION_COLUMN_ID + " = " + id, null,
                null, null, null);
        if(cursor == null)
            return null;
        cursor.moveToFirst();
        Section section = cursorToSection(cursor);
        cursor.close();
        return section;
    }

//    public Section createSection(String title, String date, String answers) {
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteOpenHelper.COLUMN_TITLE, title);
//        values.put(MySQLiteOpenHelper.COLUMN_DATE, date);
//        values.put(MySQLiteOpenHelper.COLUMN_ANSWERS, answers);
//        long insertId = database.insert(MySQLiteOpenHelper.TABLE_KEYS, null, values);
//        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_KEYS,
//                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        Section newKey = cursorToKey(cursor);
//        cursor.close();
//        return newKey;
//    }

//    public void deleteKeyById(String Id) {
//        long id = Long.parseLong(Id);
//        System.out.println("Section deleted with id: " + id);
//        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
//                + " = " + id, null);
//    }
//
//    public void deleteKey(Section section) {
//        long id = section.getId();
//        System.out.println("Section deleted with id: " + id);
//        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
//                + " = " + id, null);
//    }

    public List<Section> getSectionsByTemplateId(long tid) {
        String id = String.valueOf(tid);
        List<Section> sections = new ArrayList<Section>();

        String condition = MySQLiteOpenHelper.SECTION_COLUMN_TEMPLATE_ID + "=" + id;
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_SECTIONS, allColumns,
                condition, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Section section = cursorToSection(cursor);
            sections.add(section);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return sections;
    }

    private Section cursorToSection(Cursor cursor) {
        Section section = new Section();
        section.setId(cursor.getLong(0));
        section.setTemplate_id(cursor.getLong(1));
        section.setHeight(cursor.getInt(2));
        section.setWidth(cursor.getInt(3));
        section.setTop(cursor.getInt(4));
        section.setLeft(cursor.getInt(5));
        section.setNum_answers(cursor.getInt(6));
        return section;
    }
}



