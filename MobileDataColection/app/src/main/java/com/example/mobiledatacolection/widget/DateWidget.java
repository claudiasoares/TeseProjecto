package com.example.mobiledatacolection.widget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
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

import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

public class DateWidget
{
    private final LinearLayout screen;
    private final TextView textView;
    private final Button button;
    private final Context context;

    public DateWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version) {
        this.screen = screen;
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

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(day, month, year, name);
            }
        });
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Since I can connect from multiple devices, we store each connection instance separately
                // any time that connectionsRef's value is null (i.e. has no children) I am offline
                FirebaseDatabase database = UtilsFirebase.getDatabase();
                final DatabaseReference myConnectionsRef = database.getReference("data");
                final DatabaseReference connectedRef = database.getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            DatabaseReference con = myConnectionsRef.push();
                            con.onDisconnect().setValue(charSequence.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("DateWidget", "Listener was cancelled at .info/connected");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });
        linearLayout.addView(textView);
        linearLayout.addView(button);
        this.screen.addView(linearLayout);

    }

    void showDate(int day,  int month, int year, String name){
        DatePickerDialog mDatePicker;

        // date picker dialog
        mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(name + ": " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        mDatePicker.setTitle(name);
        mDatePicker.show();

    }

    public LinearLayout getElement(){
        return screen;
    }
}
