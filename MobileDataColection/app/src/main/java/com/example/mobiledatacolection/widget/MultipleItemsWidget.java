package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.SelectChoice;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.ArrayList;
import java.util.List;

public class MultipleItemsWidget {

    private LinearLayout screen;
    private DatabaseReference databaseReference;
    private TextView tv1;
    List<CheckBox> listCb ;

    public MultipleItemsWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version, DatabaseReference databaseReference){
        // nome da label
        // opções com checkbox??
        this.screen = screen;
        this.databaseReference = databaseReference;
        this.listCb=new ArrayList<>();
        String name = fep.mTreeElement.getName();
        tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        List<SelectChoice> choices = form.getChoices();
        this.screen.addView(tv1);
        for(SelectChoice sc : choices){
            CheckBox cb = new CheckBox(context);
            cb.setText(sc.getValue());
            listCb.add(cb);
            this.screen.addView(cb);
        }

        for (CheckBox cb : listCb) {


            cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final DatabaseReference connectedRef = databaseReference.getDatabase().getReference(".info/connected");
                    connectedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean connected = snapshot.getValue(Boolean.class);
                            if (connected) {
                                for (CheckBox cbs : listCb){
                                    databaseReference.child(cbs.getText().toString()).onDisconnect().setValue(cbs.isChecked());
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }


            });
        }

    }
    public LinearLayout getElement(){
        return screen;
    }
}
