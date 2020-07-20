package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;

public class BooleanWidget{
    private final LinearLayout screen;
    private final DatabaseReference databaseReference;
    private final long delay = 1000; // 1 seconds after user stops typing
    private long last_text_edit = 0;
    private final Handler handler = new Handler();
    private TextView tv1;
    private RadioButton edq;
    public BooleanWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryController fep, int version, DatabaseReference databaseReference) {
        //super(context);
        this.screen = screen;
        this.databaseReference = databaseReference;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        tv1.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged (CharSequence s,int start, int count,
                                          int after){
           }
           @Override
           public void onTextChanged ( final CharSequence s, int start, int before,
                                       int count){
               //You need to remove this to run only once
               handler.removeCallbacks(input_finish_checker);

           }
           @Override
           public void afterTextChanged ( final Editable s){
               //avoid triggering event when text is empty
               if (s.length() > 0) {
                   last_text_edit = System.currentTimeMillis();
                   handler.postDelayed(input_finish_checker, delay);
               } else {

               }
           }
       }

        );
        edq = new RadioButton(context);
        screen.addView(tv1);
        screen.addView(edq);
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                final DatabaseReference connectedRef = databaseReference.getDatabase().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            databaseReference.child(tv1.getText().toString()).onDisconnect().setValue(edq.isChecked());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    };
    public LinearLayout getElement(){
        return screen;
    }
}
