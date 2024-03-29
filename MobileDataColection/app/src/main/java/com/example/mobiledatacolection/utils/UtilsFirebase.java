package com.example.mobiledatacolection.utils;

import com.google.firebase.database.FirebaseDatabase;

public class UtilsFirebase {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
