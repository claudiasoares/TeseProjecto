package com.example.mobiledatacolection.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.fragmentos.ListFormsSavedFragment;
import com.example.mobiledatacolection.model.FormsFill;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.example.mobiledatacolection.sqlLite.crudOperations.CrudFormsFill;
import com.example.mobiledatacolection.utils.FileUtils;
import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.example.mobiledatacolection.widget.QuestionDetails;
import com.example.mobiledatacolection.widget.WidgetFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.IDataReference;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.AnswerDataFactory;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryModel;
import org.javarosa.form.api.FormEntryPrompt;
import org.javarosa.xform.util.XFormUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    private HashMap<Integer, HashMap<Integer, Class>> hash;
    private FormDef formDef;
    private LinearLayout ll;
    private FormEntryModel model;
    private ScrollView scroll;
    // Write a message to the database
    private FirebaseDatabase database;
    private String user;
    private String company;
    private int version;
    private String createdon;
    private DatabaseReference databaseReference;
    private String fileNameWithoutExtension;
    private long tempoInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        String fileName;
        company = getIntent().getStringExtra("COMPANY");
        user = getIntent().getStringExtra("USERNAME");
        version = getIntent().getIntExtra("VERSION",0);
        createdon = getIntent().getStringExtra("CREATEDON");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                fileName= null;
            } else {
                fileName= extras.getString("name_of_file");
            }
        } else {
            fileName= (String) savedInstanceState.getSerializable("name_of_file");
        }
        tempoInicial = System.currentTimeMillis();

        fileNameWithoutExtension = fileName.split("\\.")[0];
        if(createdon != null){
            databaseReference = UtilsFirebase.getDatabase()
                    .getReference("/company/" + company + "/user/"+user+"/"+fileNameWithoutExtension+"/"+createdon+"/");

        }
        doRenderForm(fileName, databaseReference);
    }

    private void doRenderForm(String fileName, DatabaseReference databaseReference) {
        WidgetFactory widget = new WidgetFactory();
        hash = widget.getHashMapControlType();
        try {
            File appFilesDirectory = getFilesDir();
            String pathNameFile = appFilesDirectory + "/" + fileName;
            File formXml = new File(pathNameFile);
            String lastSavedSrc = FileUtils.getOrCreateLastSavedSrc(formXml);
            formDef = XFormUtils.getFormFromFormXml(pathNameFile, lastSavedSrc);

            model = new FormEntryModel(formDef);
            FormIndex index = model.getFormIndex();
            FormEntryCaption fec = new FormEntryCaption(formDef,index);
            FormEntryController form = new FormEntryController(model);


            ll = new LinearLayout(this);

            LinearLayout.LayoutParams layoutForInner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutForInner.setMargins(10,5,10,0);
            ll.setLayoutParams(layoutForInner);
            ll.setOrientation(LinearLayout.VERTICAL);

            recursividade(formDef, form, index,databaseReference);

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(fileName);
            scroll = (ScrollView) findViewById(R.id.scrollView);
            Button buttonSubmit = new Button(this);
            buttonSubmit.setText(R.string.buttonTextSubmmit);
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLLiteDBHelper sqlLite = new SQLLiteDBHelper(getBaseContext());
                    CrudFormsFill crud = new CrudFormsFill(sqlLite);
                    int rows = crud.update(new FormsFill(fileName,company,null,version,createdon,SQLLiteDBHelper.STATE_FORM_SUBMITTED,user));
                    if(rows == 0) Toast.makeText(getBaseContext(),"Not updated! ",Toast.LENGTH_LONG);
                    Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                    intent.putExtra("menu", R.id.navigation_formssaved);
                    startActivity(intent);
                    // To-Do change status form

                }
            });
            ll.addView(buttonSubmit);
            scroll.addView(ll);
            System.out.println("o form renderizou em " + (System.currentTimeMillis() - tempoInicial) + " ms");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private IFormElement recursividade(IFormElement form, FormEntryController formEntryController, FormIndex index, DatabaseReference databaseReference){
        List<IFormElement> childrens = form.getChildren();

        if( childrens == null){
            IDataReference bind = form.getBind();
            List<TreeElement> attributes = form.getAdditionalAttributes();
            // IAnswerData answerData = new AnswerDataFactory()
            FormEntryPrompt fep = new FormEntryPrompt(formDef,index);
            int datatype = fep.getDataType();

            //fep.getDataType();
            QuestionDef qd = (QuestionDef)form;

            Class c = hash.get(qd.getControlType()).get(datatype);
            Constructor constructor[] = c.getConstructors();
            QuestionDetails questionDetails = new QuestionDetails(fep);
            Object[] intArgs = new Object[] { this , ll, qd, fep, version,databaseReference };
            Class noparams[] = {};
            try {
                Object instance = constructor[0].newInstance(intArgs);
                Method method = c.getDeclaredMethod("getElement", noparams);
                ll = (LinearLayout) method.invoke(instance);
            } catch (Exception e) {
                Log.e("Form Activity", e.getMessage());
            }
            Log.v("Form Activity", form.getLabelInnerText());
            return null;
        }
        FormEntryController formController;
        for(IFormElement child : childrens){
            index = model.incrementIndex(index,true);
            recursividade(child,formEntryController, index, databaseReference);
        }
        return null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("start");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferencesServer = getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesServer.edit();
        editor.putLong("Latitude", 0);
        editor.putLong("Longitude", 0);
    }
}
