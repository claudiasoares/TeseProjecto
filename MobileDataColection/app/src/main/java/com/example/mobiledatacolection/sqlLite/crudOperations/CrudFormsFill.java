package com.example.mobiledatacolection.sqlLite.crudOperations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.model.FormsFill;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_CATEGORY;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_COMPANY;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_ID;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_USER;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_VERSION;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_FILENAME;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_CREATEDON;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_COLUMN_STATE;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMSFILL_TABLE_NAME;

public class CrudFormsFill implements ICrudOperations{



    private final SQLLiteDBHelper sqlhelper;

    public CrudFormsFill(SQLLiteDBHelper sqlhelper){
        this.sqlhelper = sqlhelper;
    }
    @Override
    public ArrayList<FormsFill> readAll() {
        SQLiteDatabase database = sqlhelper.getReadableDatabase();
        String[] columns = new String[]{FORMSFILL_COLUMN_ID, FORMSFILL_COLUMN_FILENAME ,
                FORMSFILL_COLUMN_COMPANY,
                FORMSFILL_COLUMN_VERSION ,
                FORMSFILL_COLUMN_CATEGORY ,
                FORMSFILL_COLUMN_STATE ,
                FORMSFILL_COLUMN_CREATEDON ,
        FORMSFILL_COLUMN_USER};
        Cursor cursor = database.query(FORMSFILL_TABLE_NAME, columns,null,null,null,null,null);
        ArrayList<FormsFill> list = new ArrayList<FormsFill>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String filename = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_FILENAME));
                String company = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_COMPANY));
                String category = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CATEGORY));
                int version = cursor.getInt(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_VERSION));
                String createdon = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CREATEDON));
                String state = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_STATE));
                String user = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_USER));
                list.add(new FormsFill(filename, company, category, version, createdon,state,user));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public ArrayList<FormsFill> readAllNew() {
        SQLiteDatabase database = sqlhelper.getReadableDatabase();
        String[] columns = new String[]{FORMSFILL_COLUMN_ID, FORMSFILL_COLUMN_FILENAME ,
                FORMSFILL_COLUMN_COMPANY,
                FORMSFILL_COLUMN_VERSION ,
                FORMSFILL_COLUMN_CATEGORY ,
                FORMSFILL_COLUMN_STATE ,
                FORMSFILL_COLUMN_CREATEDON ,
                FORMSFILL_COLUMN_USER};
        Cursor cursor = database.query(FORMSFILL_TABLE_NAME, columns,FORMSFILL_COLUMN_STATE +" = '"+SQLLiteDBHelper.STATE_FORM_NEW +"'",null,null,null,null);
        ArrayList<FormsFill> list = new ArrayList<FormsFill>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String filename = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_FILENAME));
                String company = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_COMPANY));
                String category = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CATEGORY));
                int version = cursor.getInt(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_VERSION));
                String createdon = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CREATEDON));
                String state = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_STATE));
                String user = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_USER));
                list.add(new FormsFill(filename, company, category, version, createdon,state,user));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public ArrayList<FormsFill> readAllSubmitted() {
        SQLiteDatabase database = sqlhelper.getReadableDatabase();
        String[] columns = new String[]{FORMSFILL_COLUMN_ID, FORMSFILL_COLUMN_FILENAME ,
                FORMSFILL_COLUMN_COMPANY,
                FORMSFILL_COLUMN_VERSION ,
                FORMSFILL_COLUMN_CATEGORY ,
                FORMSFILL_COLUMN_STATE ,
                FORMSFILL_COLUMN_CREATEDON ,
                FORMSFILL_COLUMN_USER};
        Cursor cursor = database.query(FORMSFILL_TABLE_NAME, columns,FORMSFILL_COLUMN_STATE +" = '"+SQLLiteDBHelper.STATE_FORM_SUBMITTED +"'",null,null,null,null);
        ArrayList<FormsFill> list = new ArrayList<FormsFill>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String filename = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_FILENAME));
                String company = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_COMPANY));
                String category = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CATEGORY));
                int version = cursor.getInt(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_VERSION));
                String createdon = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMSFILL_COLUMN_CREATEDON));
                String state = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_STATE));
                String user = cursor.getString(cursor.getColumnIndex(FORMSFILL_COLUMN_USER));
                list.add(new FormsFill(filename, company, category, version, createdon,state,user));
                cursor.moveToNext();
            }
        }
        return list;
    }

    @Override
    public Object read(Object o) throws Exception {
        return null;
    }

    @Override
    public void write(List t) throws Exception {

    }

    public void write(ArrayList<FormsFill> listForms) throws Exception {
        for (int i =0; i < listForms.size(); ++i){
            ContentValues values = new ContentValues();
            values.put(FORMSFILL_COLUMN_FILENAME, listForms.get(i).getFilename());
            values.put(FORMSFILL_COLUMN_COMPANY, listForms.get(i).getCompany());
            values.put(FORMSFILL_COLUMN_CATEGORY, listForms.get(i).getCategory());
            values.put(FORMSFILL_COLUMN_VERSION, listForms.get(i).getVersion());
            values.put(FORMSFILL_COLUMN_CREATEDON, listForms.get(i).getCreatedon());
            values.put(FORMSFILL_COLUMN_STATE, listForms.get(i).getState());
            values.put(FORMSFILL_COLUMN_USER, listForms.get(i).getUser());
            long newRowId = sqlhelper.getWritableDatabase().insert(SQLLiteDBHelper.FORMSFILL_TABLE_NAME, null, values);
        }
    }

    @Override
    public int update(Object o) {
        FormsFill status = (FormsFill) o;
        if(status.getState().equals(SQLLiteDBHelper.STATE_FORM_NEW) || status.getState().equals(SQLLiteDBHelper.STATE_FORM_SUBMITTED)){
            ContentValues cv = new ContentValues();
            cv.put(FORMSFILL_COLUMN_STATE, status.getState()); //These Fields should be your String values of actual column names
            sqlhelper
                    .getReadableDatabase()
                    .execSQL("UPDATE formsFill set state = '"+status.getState()+"' WHERE filename = '"+status.getFilename()+"' AND createdon = '"+ status.getCreatedon()+"' AND version = "+ status.getVersion()+" AND company = '" + status.getCompany()+"'");
        }
        return 0;
    }

    @Override
    public void delete(Object o) {
        FormsFill status = (FormsFill) o;
        if(status.getState().equals(SQLLiteDBHelper.STATE_FORM_NEW)){
            sqlhelper
                    .getReadableDatabase()
                    .execSQL("DELETE FROM formsFill WHERE filename = '"+status.getFilename()+"' AND createdon = '"+ status.getCreatedon()+"' AND version = "+ status.getVersion()+" AND company = '" + status.getCompany()+"'");
        }
    }
}
