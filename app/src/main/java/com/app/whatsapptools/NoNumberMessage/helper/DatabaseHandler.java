package com.app.whatsapptools.NoNumberMessage.helper;

/**
 * Created by Baljeet on 28-01-2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.whatsapptools.NoNumberMessage.Models.SqLiteText;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "numberManager";

    // Contacts table name
    private static final String TABLE_NUMBERS = "phone_number";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_CODE = "code";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NUMBERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " TEXT,"
                + KEY_CODE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addNumber(SqLiteText sqLiteText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, sqLiteText.getNumber()); // Repeated Text
        values.put(KEY_CODE, sqLiteText.getCode());

        // Inserting Row
        db.insert(TABLE_NUMBERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public SqLiteText getNumber(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NUMBERS, new String[] { KEY_ID,
                        KEY_NUMBER }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SqLiteText sqLiteText = new SqLiteText(Integer.parseInt(cursor.getString(0)), cursor.getString(1)
        , cursor.getString(2));
        // return contact
        return sqLiteText;
    }

    // Getting All Contacts
    public ArrayList<SqLiteText> getAllNumbers() {
        ArrayList<SqLiteText> contactList = new ArrayList<SqLiteText>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NUMBERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SqLiteText sqLiteText = new SqLiteText();
                sqLiteText.setId(Integer.parseInt(cursor.getString(0)));
                sqLiteText.setNumber(cursor.getString(1));
                sqLiteText.setCode(cursor.getString(2));
                // Adding contact to list
                contactList.add(sqLiteText);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateNumber(SqLiteText sqLiteText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, sqLiteText.getNumber());

        // updating row
        return db.update(TABLE_NUMBERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sqLiteText.getId()) });
    }

    // Deleting single contact
    public void deleteNumber(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NUMBERS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    // Getting contacts Count
    public int getNumbersCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_NUMBERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}