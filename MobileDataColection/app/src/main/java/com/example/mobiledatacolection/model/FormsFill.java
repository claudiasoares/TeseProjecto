package com.example.mobiledatacolection.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mobiledatacolection.sqlLite.crudOperations.CrudFormsFill;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FormsFill {

    private final String user;
    private String filename;
    private String company;
    private String category;
    private int version;
    private String createdon;
    private String state;

    public static List<FormsFill> parseFormData(JSONArray forms, Context context) {

        List<FormsFill> formList = new ArrayList<>();
        return formList;
    }



    public String getFilename() {
        return filename;
    }

    public String getCompany() {
        return company;
    }

    public String getCategory() {
        return category;
    }

    public int getVersion() {
        return version;
    }

    public String getCreatedon() {
        return createdon;
    }

    public String getState() {
        return state;
    }

    public String getUser() {
        return user;
    }



    public FormsFill(String filename, String company, String category, int version, String createdon, String state, String user) {
        this.filename = filename;
        this.company = company;
        this.category = category;
        this.version = version;
        this.createdon = createdon;
        this.state = state;
        this.user = user;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        FormsFill f = (FormsFill) obj;
        return f.filename.equals(filename) &&  f.version == version && f.getCreatedon() == createdon;
    }
}
