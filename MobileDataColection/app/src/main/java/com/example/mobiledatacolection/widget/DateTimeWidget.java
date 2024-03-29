package com.example.mobiledatacolection.widget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;
import java.util.Map;

public class DateTimeWidget  {
    private final LinearLayout screen;
    private final Context context;
    private final TextView textView;
    private final Button button;
    private static final IntentFilter s_intentFilter ;
    private final long delay = 1000; // 1 seconds after user stops typing
    private long last_text_edit = 0;
    private final Handler handler = new Handler();
    String name;
    Calendar mCalendar;

    static {
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    private final DatabaseReference databaseReference;

    public DateTimeWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version, DatabaseReference databaseReference) {
       // super(context,qd);
        this.screen = screen;
        mCalendar = Calendar.getInstance();
        this.databaseReference = databaseReference;
        name = fep.mTreeElement.getName();
        // String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        this.context = context;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        params.setMargins(0, 5, 5, 5);

        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(name);
        databaseReference.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(name + ": " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        LinearLayout.LayoutParams paramsTextView
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(params);
        textView.setLayoutParams(paramsTextView);

        button = new Button(context);
        LinearLayout.LayoutParams paramsButton
                = new LinearLayout.LayoutParams(40, 100, 1);

        linearLayout.setLayoutParams(params);
        button.setLayoutParams(paramsButton);
        button.setBackgroundColor(Color.LTGRAY);
        button.setText("Select " + name);

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        button.setOnClickListener(buttonList);

        linearLayout.addView(textView);
        linearLayout.addView(button);
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
        this.screen.addView(linearLayout);

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
                            databaseReference.child(textView.getText().toString().split(": ")[0]).onDisconnect().setValue(textView.getText().toString().split(": ")[1]);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    };

    void showDate(int day, int month, int year, String name) {
        DatePickerDialog mDatePicker;

        // date picker dialog
        mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(name + ": " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                new TimePickerDialog(context, mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
            }
        }, year, month, day);
        mDatePicker.setTitle(name);
        mDatePicker.show();

    }

    /* Define the onClickListener, and start the DatePickerDialog with users current time */
    private final View.OnClickListener buttonList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(context, mDateDataSet, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };
    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(context, mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            String AM_PM ;
            if(hourOfDay < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }
            textView.setText(name + ": " + mCalendar.get(Calendar.YEAR) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.DAY_OF_MONTH) + " " + mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE) + " "+ AM_PM);

        }
    };

    public LinearLayout getElement(){
        return screen;
    }
}
