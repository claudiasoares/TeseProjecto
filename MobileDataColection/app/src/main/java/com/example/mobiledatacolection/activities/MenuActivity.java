package com.example.mobiledatacolection.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Parcelable;
import android.telephony.mbms.FileInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.adapters.WidgetFragmentCollectionAdapter;
import com.example.mobiledatacolection.fragmentos.ListFormsFragment;
import com.example.mobiledatacolection.fragmentos.MenuFragment;
import com.example.mobiledatacolection.fragmentos.NotificationFragment;
import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.fragmentos.WidgetFragment;
import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.tasks.FormController;
import com.example.mobiledatacolection.tasks.FormLoaderTask;
import com.example.mobiledatacolection.widget.WidgetFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.mobiledatacolection.utils.FileUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.IDataReference;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.condition.Constraint;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.xform.util.XFormUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.*;

import org.xmlpull.v1.XmlPullParser;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    WidgetFactory widget;
    private HashMap<Integer, HashMap<Integer, Class>> hash;
    private LinearLayout ll;

    private Button loadForm;
    BottomNavigationView bottomNavigation;
    private FormDef formDef;
    FormEntryModel model;
    TabLayout tabLayout;
    WidgetFragmentCollectionAdapter adapter;
    ViewPager page;
    private String company;
    private String user;

    @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                     openFragment( MenuFragment.newInstance());
                    return true;
                case R.id.navigation_forms:
                    ArrayList <Forms> files = readAllFilesFileExplorer();
                    openFragment(ListFormsFragment.newInstance(files,company,user));
                    return true;
                case R.id.navigation_loadforms:
                    openFragment(NotificationFragment.newInstance("", ""));
                    return true;
            }
            return false;
        }

    private ArrayList <Forms> readAllFilesFileExplorer() {
        File appFilesDirectory = getFilesDir();
        ArrayList <Forms> existsFiles = new ArrayList<Forms>();
        File[] files = appFilesDirectory.listFiles();
        for (File file: files ) {
            if(file.isFile())
                existsFiles.add(new Forms(file.getName(), company, null, null, 0, null));
        }
        return existsFiles;
    }

    private String findMatch(String myString) {

        String match = "";

        // Pattern to find code
        String pattern = "[A-Za-z]+:";  // Sequence of 8 digits
        Pattern regEx = Pattern.compile(pattern);

        // Find instance of pattern matches
        Matcher m = regEx.matcher(myString);
        if (m.find()) {
            match = m.group(0);
        }
        return match;
    }

    @Nullable
    private FormController getFormController() {
        return getFormController(false);
    }

    @Nullable
    private FormController getFormController(boolean formReloading) {
        FormController formController = MobileDataCollect.getInstance().getFormController();
        if (formController == null) {

        }

        return formController;
    }



    @SuppressLint("ResourceAsColor")
    private void createNewActivity(String name){

        //openFragment(MenuFragment.newInstance(linearLayout));



    }


    /** Suffix for the form media directory. */
    public static final String MEDIA_SUFFIX = "-media";

    /** Filename of the last-saved instance data. */
    public static final String LAST_SAVED_FILENAME = "testetese.xml";

    /** Valid XML stub that can be parsed without error. */
    private static final String STUB_XML = "<?xml version='1.0' ?><stub />";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        company = getIntent().getStringExtra("COMPANY");
        user = getIntent().getStringExtra("USERNAME");
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        //initComponents();

    }

    private void processParsing(XmlPullParser p) {
        ArrayList<Object> obj = new ArrayList<>();
        int eventType = 0;
        try {
            eventType = p.getEventType();

        while(eventType != XmlPullParser.END_DOCUMENT){
            System.out.println(p.getName());
            eventType=p.next();

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    private void readFile(){

    }

    private FormDef createFormDefFromCacheOrXml(String formPath, File formXml) {

        String lastSavedSrc = getOrCreateLastSavedSrc(formXml);
        FormDef formDefFromXml = XFormUtils.getFormFromFormXml(formPath, lastSavedSrc);
        if (formDefFromXml == null) {
        } else {
            formDef = formDefFromXml;

            return formDefFromXml;
        }

        return null;
    }

    /**
     * Returns the path to the last-saved file for this form,
     * creating a valid stub if it doesn't yet exist.
     */
    public static String getOrCreateLastSavedSrc(File formXml) {
        File lastSavedFile = getLastSavedFile(formXml);

        if (!lastSavedFile.exists()) {
            write(lastSavedFile, STUB_XML.getBytes(Charset.forName("UTF-8")));
        }

        return "jr://file/" + LAST_SAVED_FILENAME;
    }
    public static File getLastSavedFile(File formXml) {
        return new File(getFormMediaDir(formXml), LAST_SAVED_FILENAME);
    }

    public static void write(File file, byte[] data) {
        // Make sure the directory path to this file exists.
        file.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.close();
        } catch (IOException e) {
        }
    }
    public static File getFormMediaDir(File formXml) {
        final String formFileName = getFormBasename(formXml);
        return new File(formXml.getParent(), formFileName + MEDIA_SUFFIX);
    }
    public static String getFormBasename(File formXml) {
        return getFormBasename(formXml.getName());
    }

    public static String getFormBasename(String formFilePath) {
        return formFilePath.substring(0, formFilePath.lastIndexOf('.'));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void initComponents() {
        loadForm = (Button) findViewById(R.id.carregarformulario) ;
        loadForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadForm(v);
            }
        });
    }

    private void loadForm(View v) {

    }


}
