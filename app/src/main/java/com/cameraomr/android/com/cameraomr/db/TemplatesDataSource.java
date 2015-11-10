package com.cameraomr.android.com.cameraomr.db;

/**
 * Created by harsha on 10/11/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TemplatesDataSource {
    private SQLiteDatabase database;
    private MySQLiteOpenHelper dbHelper;
    private Context mContext;
    private String[] allColumns = { MySQLiteOpenHelper.TEMPLATE_COLUMN_ID,
            MySQLiteOpenHelper.TEMPLATE_COLUMN_HEIGHT, MySQLiteOpenHelper.TEMPLATE_COLUMN_WIDTH,
            MySQLiteOpenHelper.TEMPLATE_COLUMN_NUM_ANSWERS,  MySQLiteOpenHelper.TEMPLATE_COLUMN_NUM_OPTIONS};

    public TemplatesDataSource(Context context) {
        dbHelper = new MySQLiteOpenHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Template getTemplate(String id)
    {
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_TEMPLATES,
                allColumns, MySQLiteOpenHelper.TEMPLATE_COLUMN_ID + " = " + id, null,
                null, null, null);
        if(cursor == null)
            return null;
        cursor.moveToFirst();
        Template template = cursorToTemplate(cursor);
        cursor.close();
        return template;
    }

//    public Template createTemplate(String title, String date, String answers) {
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteOpenHelper.COLUMN_TITLE, title);
//        values.put(MySQLiteOpenHelper.COLUMN_DATE, date);
//        values.put(MySQLiteOpenHelper.COLUMN_ANSWERS, answers);
//        long insertId = database.insert(MySQLiteOpenHelper.TABLE_KEYS, null, values);
//        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_KEYS,
//                allColumns, MySQLiteOpenHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        Template newKey = cursorToKey(cursor);
//        cursor.close();
//        return newKey;
//    }

//    public void deleteKeyById(String Id) {
//        long id = Long.parseLong(Id);
//        System.out.println("Template deleted with id: " + id);
//        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
//                + " = " + id, null);
//    }
//
//    public void deleteKey(Template template) {
//        long id = template.getId();
//        System.out.println("Template deleted with id: " + id);
//        database.delete(MySQLiteOpenHelper.TABLE_KEYS, MySQLiteOpenHelper.COLUMN_ID
//                + " = " + id, null);
//    }

    public List<Template> getAllTemplates() {
        List<Template> templates = new ArrayList<Template>();

        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_TEMPLATES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Template template = cursorToTemplate(cursor);
            templates.add(template);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return templates;
    }

    private Template cursorToTemplate(Cursor cursor) {
        Template template = new Template();
        template.setId(cursor.getLong(0));
        template.setHeight(cursor.getInt(1));
        template.setWidth(cursor.getInt(2));
        template.setNum_answers(cursor.getInt(3));
        template.setNum_options(cursor.getInt(4));

        SectionsDataSource sectionsdatasource = new SectionsDataSource(mContext);
        sectionsdatasource.open();
        List<Section> sections = sectionsdatasource.getSectionsByTemplateId(template.getId());
        template.setSections(sections);
        sectionsdatasource.close();
        return template;
    }
}


