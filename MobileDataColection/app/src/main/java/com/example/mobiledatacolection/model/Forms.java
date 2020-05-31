package com.example.mobiledatacolection.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mobiledatacolection.dto.Form;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forms {

    private String filename;
    private String company;
    private String category;
    private String description;
    private int version;
    private String files;

    public static List<Forms> parseFormData(JSONArray forms, Context context) {
        List<Forms> formList = new ArrayList<Forms>();
        try {

            if (forms != null) {
                for (int i = 0; i < forms.length(); i++) {
                    String filename = forms.getJSONObject(i).getString("filename");
                    String company = forms.getJSONObject(i).getString("company");
                    String category = forms.getJSONObject(i).getString("category");
                    String description = forms.getJSONObject(i).getString("description");
                    int version = forms.getJSONObject(i).getInt("version");
                    String files = forms.getJSONObject(i).getString("files");
                    //the value of progress is a placeholder here....
                    Forms form = new Forms(filename,company,category,description,version,files);
                    writeFileOnInternalStorage(context,filename,files);
                    writeFirebase(form);
                    formList.add(form);
                    Log.v("FormList", "Filename " + filename + "company " + company + "category " + category + "description" + description);
                }

            }

        } catch(JSONException e) {
            Log.e("CatalogForm", "unexpected JSON exception", e);
        }
        return formList;
    }

    public static void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = mcoContext.getFilesDir();
        if(!dir.exists()){
            dir.mkdir();
        }

        try{
            File file = new File(mcoContext.getFilesDir(),sFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public static void writeFirebase(Forms form){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("forms");
        String filename = form.filename.split("\\.")[0];
        DatabaseReference formRef = myRef.child(filename);
        Map<String, Forms> hash = new HashMap<String, Forms>();
        hash.put(filename, form);
        formRef.setValue(hash);
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

    public String getDescription() {
        return description;
    }

    public String getFiles() {
        return files;
    }

    public Forms(String filename, String company, String category, String description, int version, String files) {
        this.filename = filename;
        this.company = company;
        this.category = category;
        this.description = description;
        this.version = version;
        this.files = files;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Forms f = (Forms) obj;
        return f.filename.equals(filename) /*&&  f.version == version*/;
    }
}
