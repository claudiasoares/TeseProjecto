package com.example.mobiledatacolection.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobiledatacolection.Fragmentos.MenuFragment;
import com.example.mobiledatacolection.Fragmentos.NotificationFragment;
import com.example.mobiledatacolection.Fragmentos.SmsFragment;
import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.dto.Instance;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.mobiledatacolection.utils.FileUtils;

import org.javarosa.core.model.FormDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.xform.util.XFormUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MenuActivity extends AppCompatActivity {

    private Button loadForm;
    BottomNavigationView bottomNavigation;
    private FormDef formDef;

    /** Suffix for the form media directory. */
    public static final String MEDIA_SUFFIX = "-media";

    /** Filename of the last-saved instance data. */
    public static final String LAST_SAVED_FILENAME = "testetese.xml";

    /** Valid XML stub that can be parsed without error. */
    private static final String STUB_XML = "<?xml version='1.0' ?><stub />";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_menu:
                                openFragment(MenuFragment.newInstance("", ""));

                                try {
                                    //XmlPullParserFactory parser = XmlPullParserFactory.newInstance();
                                   // XmlPullParser p  = parser.newPullParser();
                                   // InputStream i = getAssets().open("testetese.xml");


                                   // FormDef def = XFormUtils.getFormFromInputStream(i, i.toString());
                                    FormDef formDef;

                                    File formXml = new File("testetese1.xml");
                                    String lastSavedSrc = FileUtils.getOrCreateLastSavedSrc(formXml);
                                    formDef = XFormUtils.getFormFromFormXml("/data/data/com.example.mobiledatacolection/files/testetese1.xml", lastSavedSrc);
                                    FormEntryModel fem = new FormEntryModel(formDef);
                                    FormEntryController fec = new FormEntryController(fem);
                                    File instanceFile = new File("teste1.xml");
                                    FormLoaderTask.importData(instanceFile, fec);
                                    //p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                                    //p.setInput(i,null);
                                    //processParsing(p);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //createFormDefFromCacheOrXml("/Users/claudiasoares/TeseProjecto/MobileDataColection/app/src/main/res/xml",new File("testetese.xml"));
                                return true;
                            case R.id.navigation_forms:
                                openFragment(SmsFragment.newInstance("", ""));
                                readFile();
                                return true;
                            case R.id.navigation_loadforms:
                                openFragment(NotificationFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

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
