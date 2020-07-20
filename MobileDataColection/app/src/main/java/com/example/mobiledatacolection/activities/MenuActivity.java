package com.example.mobiledatacolection.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Parcelable;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.telephony.mbms.FileInfo;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import com.example.mobiledatacolection.fragmentos.ListFormsSavedFragment;
import com.example.mobiledatacolection.fragmentos.MenuFragment;
import com.example.mobiledatacolection.fragmentos.NotificationFragment;
import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.fragmentos.WidgetFragment;
import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.model.FormsFill;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.example.mobiledatacolection.sqlLite.crudOperations.CrudForms;
import com.example.mobiledatacolection.sqlLite.crudOperations.CrudFormsFill;
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
            int menu=-1;
            SQLLiteDBHelper sqlLite = new SQLLiteDBHelper(this);
            if( getIntent().getExtras().getInt("menu") == 0 ){
                menu = item.getItemId();
            }else{
                menu = getIntent().getExtras().getInt("menu");
            }


            switch (menu) {
                case R.id.navigation_menu:
                     openFragment( MenuFragment.newInstance());
                    return true;
                case R.id.navigation_forms:

                    // ArrayList <Forms> files = readAllFilesFileExplorer();
                    ArrayList <Forms> files = readAllFilesSQLLite(sqlLite);
                    openFragment(ListFormsFragment.newInstance(files,company,user,new SQLLiteDBHelper(this)));
                    return true;
                case R.id.navigation_formssaved:
                    ArrayList<FormsFill> formsfill = readAllFormsFill(sqlLite);
                    openFragment(ListFormsSavedFragment.newInstance(formsfill, company,user,new SQLLiteDBHelper(this),true));
                    return true;
                case R.id.navigation_formssubmitted:
                    ArrayList<FormsFill> formsfillSubmitted = readAllFormsFillSubmitted(sqlLite);
                    openFragment(ListFormsSavedFragment.newInstance(formsfillSubmitted, company,user,new SQLLiteDBHelper(this), false));
                    return true;
            }
            return false;
        }

    private ArrayList<FormsFill> readAllFormsFill(SQLLiteDBHelper sqlLite) {
        return new CrudFormsFill(sqlLite).readAllNew();
    }

    private ArrayList<FormsFill> readAllFormsFillSubmitted(SQLLiteDBHelper sqlLite) {
        return new CrudFormsFill(sqlLite).readAllSubmitted();
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

    private ArrayList <Forms> readAllFilesSQLLite(SQLLiteDBHelper sqlLite) {
        ArrayList <Forms> existsFiles = new CrudForms(sqlLite).readAll();
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
        if( getIntent().getExtras().getInt("menu") != 0 ){
            onNavigationItemSelected(new MenuItem() {
                @Override
                public int getItemId() {
                    return R.id.navigation_formssaved;
                }

                @Override
                public int getGroupId() {
                    return 0;
                }

                @Override
                public int getOrder() {
                    return 0;
                }

                @Override
                public MenuItem setTitle(CharSequence title) {
                    return null;
                }

                @Override
                public MenuItem setTitle(int title) {
                    return null;
                }

                @Override
                public CharSequence getTitle() {
                    return null;
                }

                @Override
                public MenuItem setTitleCondensed(CharSequence title) {
                    return null;
                }

                @Override
                public CharSequence getTitleCondensed() {
                    return null;
                }

                @Override
                public MenuItem setIcon(Drawable icon) {
                    return null;
                }

                @Override
                public MenuItem setIcon(int iconRes) {
                    return null;
                }

                @Override
                public Drawable getIcon() {
                    return null;
                }

                @Override
                public MenuItem setIntent(Intent intent) {
                    return null;
                }

                @Override
                public Intent getIntent() {
                    return null;
                }

                @Override
                public MenuItem setShortcut(char numericChar, char alphaChar) {
                    return null;
                }

                @Override
                public MenuItem setNumericShortcut(char numericChar) {
                    return null;
                }

                @Override
                public char getNumericShortcut() {
                    return 0;
                }

                @Override
                public MenuItem setAlphabeticShortcut(char alphaChar) {
                    return null;
                }

                @Override
                public char getAlphabeticShortcut() {
                    return 0;
                }

                @Override
                public MenuItem setCheckable(boolean checkable) {
                    return null;
                }

                @Override
                public boolean isCheckable() {
                    return false;
                }

                @Override
                public MenuItem setChecked(boolean checked) {
                    return null;
                }

                @Override
                public boolean isChecked() {
                    return false;
                }

                @Override
                public MenuItem setVisible(boolean visible) {
                    return null;
                }

                @Override
                public boolean isVisible() {
                    return false;
                }

                @Override
                public MenuItem setEnabled(boolean enabled) {
                    return null;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }

                @Override
                public boolean hasSubMenu() {
                    return false;
                }

                @Override
                public SubMenu getSubMenu() {
                    return null;
                }

                @Override
                public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
                    return null;
                }

                @Override
                public ContextMenu.ContextMenuInfo getMenuInfo() {
                    return null;
                }

                @Override
                public void setShowAsAction(int actionEnum) {

                }

                @Override
                public MenuItem setShowAsActionFlags(int actionEnum) {
                    return null;
                }

                @Override
                public MenuItem setActionView(View view) {
                    return null;
                }

                @Override
                public MenuItem setActionView(int resId) {
                    return null;
                }

                @Override
                public View getActionView() {
                    return null;
                }

                @Override
                public MenuItem setActionProvider(ActionProvider actionProvider) {
                    return null;
                }

                @Override
                public ActionProvider getActionProvider() {
                    return null;
                }

                @Override
                public boolean expandActionView() {
                    return false;
                }

                @Override
                public boolean collapseActionView() {
                    return false;
                }

                @Override
                public boolean isActionViewExpanded() {
                    return false;
                }

                @Override
                public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
                    return null;
                }
            });
        }
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
