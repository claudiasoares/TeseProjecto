package com.example.mobiledatacolection.widget;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.example.mobiledatacolection.fragmentos.widget.TimePickerFragment;
import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.zip.Inflater;

import static android.widget.Toast.LENGTH_SHORT;

public class TimeWidget {
    private final LinearLayout screen;
    private final DatabaseReference databaseReference;
    private TextView textView;
    private Button button;
    int hour,min;
    private TimePickerDialog picker;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Context context;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    public TimeWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version,DatabaseReference databaseReference) {
        this.screen = screen;
        this.databaseReference = databaseReference;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        this.context = context;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        params.setMargins(0,5,5,0);

        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(name);
        LinearLayout.LayoutParams paramsTextView
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(params);
        textView.setLayoutParams(paramsTextView);

        button = new Button(context);
        LinearLayout.LayoutParams paramsButton
                = new LinearLayout.LayoutParams(40,100,1);
        linearLayout.setLayoutParams(params);
        button.setLayoutParams(paramsButton);
        button.setBackgroundColor(Color.LTGRAY);
        button.setText("Select " + name);


        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        min = mcurrentTime.get(Calendar.MINUTE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime(hour,min, name);
            }
        });
        textView.addTextChangedListener(new TextWatcher() {
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
        });
        databaseReference.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(name + ": " + (dataSnapshot.getValue() == null ? "" : dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        linearLayout.addView(textView);
        linearLayout.addView(button);
        this.screen.addView(linearLayout);

    }

    void showTime(int hours,  int minte, String name){
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setText( name + ": " +selectedHour + ":" + selectedMinute);
                hour=selectedHour;
                min=selectedMinute;
            }
        }, hours, minte, false);//Yes 24 hour time
        mTimePicker.setTitle(name);
        mTimePicker.show();

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
                            databaseReference.child(
                                    textView
                                            .getText() == null ? "" : textView
                                            .getText().toString().split(": ")[0])
                                    .onDisconnect()
                                    .setValue(textView.getText().toString().split(": ").length >= 2 ? textView.getText().toString().split(": ")[1]: "");
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
