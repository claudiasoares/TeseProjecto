package com.example.mobiledatacolection.sqlLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteDBHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "mobiledatacollect";
        // Users
        public static final String USER_TABLE_NAME = "users";
        public static final String USER_COLUMN_ID = "_id";
        public static final String USER_COLUMN_USERNAME = "name";
        public static final String USER_COLUMN_COMPANY = "company";
        public static final String USER_COLUMN_PASSWORD = "password";
        // Forms
        public static final String FORMS_TABLE_NAME = "forms";
        public static final String FORMS_COLUMN_ID = "_id";
        public static final String FORMS_COLUMN_FILENAME = "filename";
        public static final String FORMS_COLUMN_VERSION = "version";
        public static final String FORMS_COLUMN_COMPANY = "company";
        public static final String FORMS_COLUMN_CATEGORY = "category";
        public static final String FORMS_COLUMN_FILE = "file";
        // Forms Fills
        // COLUMN STATE = {NEW, DELETED, SUBMITTED}
        public static final String FORMSFILL_TABLE_NAME = "formsFill";
        public static final String FORMSFILL_COLUMN_ID = "_id";
        public static final String FORMSFILL_COLUMN_FILENAME = "filename";
        public static final String FORMSFILL_COLUMN_VERSION = "version";
        public static final String FORMSFILL_COLUMN_COMPANY = "company";
        public static final String FORMSFILL_COLUMN_CATEGORY = "category";
        public static final String FORMSFILL_COLUMN_STATE = "state";
        public static final String FORMSFILL_COLUMN_CREATEDON = "createdon";
        public static final String FORMSFILL_COLUMN_USER = "user";

        public static final String STATE_FORM_NEW = "NEW";
        public static final String STATE_FORM_DELETED = "DELETED";
        public static final String STATE_FORM_SUBMITTED = "SUBMITTED";

        public SQLLiteDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (" +
                    USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_COLUMN_USERNAME + " TEXT, " +
                    USER_COLUMN_COMPANY + " TEXT, " +
                    USER_COLUMN_PASSWORD + " TEXT" + ")");
            sqLiteDatabase.execSQL("CREATE TABLE " + FORMS_TABLE_NAME + " (" +
                    FORMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FORMS_COLUMN_FILENAME + " TEXT, " +
                    FORMS_COLUMN_COMPANY + " TEXT, " +
                    FORMS_COLUMN_VERSION + " INTEGER, " +
                    FORMS_COLUMN_FILE + " TEXT, " +
                    FORMS_COLUMN_CATEGORY + " TEXT" + ")");

            sqLiteDatabase.execSQL("CREATE TABLE " + FORMSFILL_TABLE_NAME + " (" +
                    FORMSFILL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FORMSFILL_COLUMN_FILENAME + " TEXT, " +
                    FORMSFILL_COLUMN_COMPANY + " TEXT, " +
                    FORMSFILL_COLUMN_VERSION + " INTEGER, " +
                    FORMSFILL_COLUMN_CATEGORY + " TEXT, " +
                    FORMSFILL_COLUMN_STATE + " TEXT, " +
                            FORMSFILL_COLUMN_USER+ " TEXT, " +
                    FORMSFILL_COLUMN_CREATEDON + " NUMERIC" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FORMS_TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FORMSFILL_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
