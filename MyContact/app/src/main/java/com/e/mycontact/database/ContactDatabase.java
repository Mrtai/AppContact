package com.e.mycontact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDatabase extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "ContactDatabase";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "contact_data";

    public static final String TABLE_CONTACT = "contact";
    public static final String TABLE_PROFILE = "profile";
    public static final String ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_EMAIL = "email";
    public static final String COL_FACEBOOK = "facebook";
    public static final String COL_IMAGE = "image";
    public static final String COL_NOTE = "note";
    public static final String COL_SCHEDULE = "schedule";
    public static final String COL_DATE_OF_BORN = "dateofborn";

    private static final String CREATE_TABLE_CONTACT = "create table " + TABLE_CONTACT
            + " (" + ID + " integer primary key autoincrement, " + COL_NAME
            + " text not null, " + COL_PHONE + " text not null, "+COL_ADDRESS+" text, "+COL_EMAIL+" text, "+
            COL_FACEBOOK+" text,"+COL_IMAGE+" blob,"+COL_NOTE+" text,"+COL_SCHEDULE+" text,"+COL_DATE_OF_BORN+" text);";
    private static final String CREATE_TABLE_PROFILE = "create table " + TABLE_PROFILE
            + " (" + ID + " integer primary key autoincrement, " + COL_NAME
            + " text not null, " + COL_PHONE + " text not null, "+COL_ADDRESS+" text, "+COL_EMAIL+" text, "
            +COL_IMAGE+" blob,"+COL_DATE_OF_BORN+" text);";

    private static final String DB_SCHEMA = CREATE_TABLE_CONTACT;
    private static final String DB_SCHEMA1 = CREATE_TABLE_PROFILE;

    public ContactDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA);
        db.execSQL(DB_SCHEMA1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }
}