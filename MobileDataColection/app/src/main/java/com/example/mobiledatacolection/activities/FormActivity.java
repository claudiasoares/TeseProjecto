package com.example.mobiledatacolection.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.utils.FileUtils;
import com.example.mobiledatacolection.widget.WidgetFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.IDataReference;
import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        String fileName;
        company = getIntent().getStringExtra("COMPANY");
        user = getIntent().getStringExtra("USERNAME");

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
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        String fileNameWithoutExtension = fileName.split("\\.")[0];
        databaseReference.child("company").child(company).child("user").child(user).child(fileNameWithoutExtension);
        doRenderForm(fileName, databaseReference);

        //addContentView(scroll, new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT ));
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

            recursividade(formDef, form, index);

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(fileName);
            scroll = (ScrollView) findViewById(R.id.scrollView);

            scroll.addView(ll);

            // WidgetFragmentCollectionAdapter adapter = new WidgetFragmentCollectionAdapter(getFragmentManager());
            // page.setAdapter(adapter);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private IFormElement recursividade(IFormElement form, FormEntryController formEntryController, FormIndex index){
        List<IFormElement> childrens = form.getChildren();

        if( childrens == null){
            IDataReference bind = form.getBind();
            List<TreeElement> attributes = form.getAdditionalAttributes();

            FormEntryPrompt fep = new FormEntryPrompt(formDef,index);
            int datatype = fep.getDataType();
            //fep.getDataType();
            QuestionDef qd = (QuestionDef)form;
            Class c = hash.get(qd.getControlType()).get(datatype);
            Constructor constructor[] = c.getConstructors();

            Object[] intArgs = new Object[] { this , ll, qd};
            Class noparams[] = {};
            try {
                Object instance = constructor[0].newInstance(intArgs);
                Method method = c.getDeclaredMethod("getElement", noparams);
                ll = (LinearLayout) method.invoke(instance);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(form.getLabelInnerText());
            return null;
        }
        FormEntryController formController;
        for(IFormElement child : childrens){
            index = model.incrementIndex(index,true);


            recursividade(child,formEntryController, index);
        }
        return null;
    }
}
