package com.example.mobiledatacolection.sqlLite.crudOperations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.mobiledatacolection.fragmentos.ListFormsFragment.FORMS_COLUMN_CATEGORY;
import static com.example.mobiledatacolection.fragmentos.ListFormsFragment.FORMS_COLUMN_COMPANY;
import static com.example.mobiledatacolection.fragmentos.ListFormsFragment.FORMS_COLUMN_FILE;
import static com.example.mobiledatacolection.fragmentos.ListFormsFragment.FORMS_COLUMN_ID;
import static com.example.mobiledatacolection.fragmentos.ListFormsFragment.FORMS_COLUMN_VERSION;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMS_COLUMN_FILENAME;
import static com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper.FORMS_TABLE_NAME;

public class CrudForms implements ICrudOperations {

    private final SQLLiteDBHelper sqlhelper;

    public CrudForms(SQLLiteDBHelper sqlhelper){
        this.sqlhelper = sqlhelper;
    }
    @Override
    public ArrayList<Forms> readAll() {
        SQLiteDatabase database = sqlhelper.getReadableDatabase();
        String[] columns = new String[]{FORMS_COLUMN_ID, FORMS_COLUMN_CATEGORY, FORMS_COLUMN_COMPANY, FORMS_COLUMN_VERSION, FORMS_COLUMN_FILE,FORMS_COLUMN_FILENAME};
        Cursor cursor = database.query(FORMS_TABLE_NAME, columns,null,null,null,null,null);
        ArrayList<Forms> list = new ArrayList<Forms>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String filename = cursor.getString(cursor.getColumnIndex(FORMS_COLUMN_FILENAME));
                String company = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMS_COLUMN_COMPANY));
                String category = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMS_COLUMN_CATEGORY));
                int version = cursor.getInt(cursor.getColumnIndex(SQLLiteDBHelper.FORMS_COLUMN_VERSION));
                String files = cursor.getString(cursor.getColumnIndex(SQLLiteDBHelper.FORMS_COLUMN_FILE));
                list.add(new Forms(filename, company, category, null, version, files));
                cursor.moveToNext();
            }
        }
        return list;
    }

    @Override
    public Object read(Object o) throws Exception {
        return null;
    }


    public Forms read(Object o, Object o2) throws Exception {
        throw new Exception("Not Implemented read");
    }

    public Forms read(String filename) throws Exception {

        String[] columns = new String[]{FORMS_COLUMN_ID, FORMS_COLUMN_CATEGORY, FORMS_COLUMN_COMPANY, "MAX("+FORMS_COLUMN_VERSION+")", FORMS_COLUMN_VERSION, FORMS_COLUMN_FILE,FORMS_COLUMN_FILENAME};
        Cursor cursor = sqlhelper.getReadableDatabase().query(FORMS_TABLE_NAME, columns, FORMS_COLUMN_FILENAME + " = '" +
                filename + "'", null,null, null, null);
        cursor.moveToFirst();
        return cursorToForms(cursor);
    }
    private Forms cursorToForms(Cursor cursor) {
        Forms form = new Forms(cursor.getString(cursor.getColumnIndex(FORMS_COLUMN_FILENAME))
                , cursor.getString(cursor.getColumnIndex(FORMS_COLUMN_COMPANY)),
                cursor.getString(cursor.getColumnIndex(FORMS_COLUMN_CATEGORY)),
                null,
                cursor.getInt(cursor.getColumnIndex(FORMS_COLUMN_VERSION)),
                cursor.getString(cursor.getColumnIndex(FORMS_COLUMN_FILE)));
        return form;
    }
    @Override
    public void write(List o) throws Exception{
        List<Forms> listForms = (List<Forms>) o;
        for (int i =0; i < listForms.size(); ++i){
            ContentValues values = new ContentValues();
            values.put(SQLLiteDBHelper.FORMS_COLUMN_FILENAME, listForms.get(i).getFilename());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_COMPANY, listForms.get(i).getCompany());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_CATEGORY, listForms.get(i).getCategory());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_VERSION, listForms.get(i).getVersion());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_FILE, listForms.get(i).getFiles());
            long newRowId = sqlhelper.getWritableDatabase().insert(SQLLiteDBHelper.FORMS_TABLE_NAME, null, values);
        }
    }

    @Override
    public int update(Object status) {
return 0;
    }

    @Override
    public void delete(Object o) {

    }
}
