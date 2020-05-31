package com.example.mobiledatacolection.sqlLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteDBHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "mobiledatacollect";
        public static final String USER_TABLE_NAME = "users";
        public static final String USER_COLUMN_ID = "_id";
        public static final String USER_COLUMN_USERNAME = "name";
        public static final String USER_COLUMN_COMPANY = "company";
        public static final String USER_COLUMN_PASSWORD = "password";

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
