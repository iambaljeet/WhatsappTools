package com.app.whatsapptools.TextRepeater.helper;

/**
 * Created by Baljeet on 28-01-2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.app.whatsapptools.TextRepeater.Models.SqLiteText;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "textManager";

    // Contacts table name
    private static final String TABLE_REP_TEXT = "repeated_text";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_REP_TEXT = "rep_text";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_REP_TEXT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REP_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REP_TEXT);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addText(SqLiteText sqLiteText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REP_TEXT, sqLiteText.getRep_text()); // Repeated Text

        // Inserting Row
        db.insert(TABLE_REP_TEXT, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public SqLiteText getText(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REP_TEXT, new String[] { KEY_ID,
                        KEY_REP_TEXT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SqLiteText sqLiteText = new SqLiteText(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        // return contact
        return sqLiteText;
    }

    // Getting All Contacts
    public List<SqLiteText> getAllTexts() {
        List<SqLiteText> contactList = new ArrayList<SqLiteText>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REP_TEXT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SqLiteText sqLiteText = new SqLiteText();
                sqLiteText.setId(Integer.parseInt(cursor.getString(0)));
                sqLiteText.setRep_text(cursor.getString(1));
                // Adding contact to list
                contactList.add(sqLiteText);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateText(SqLiteText sqLiteText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REP_TEXT, sqLiteText.getRep_text());

        // updating row
        return db.update(TABLE_REP_TEXT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sqLiteText.getId()) });
    }

    // Deleting single contact
    public void deleteText(SqLiteText sqLiteText) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REP_TEXT, KEY_ID + " = ?",
                new String[] { String.valueOf(sqLiteText.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getTextCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_REP_TEXT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}