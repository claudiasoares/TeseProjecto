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
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.example.mobiledatacolection.fragmentos.widget.TimePickerFragment;

import org.javarosa.core.model.QuestionDef;

import java.io.LineNumberReader;
import java.util.zip.Inflater;

public class TimeWidget {
    private final LinearLayout screen;
    private TextView textView;
    private Button button;
    int hour,min;
    private TimePickerDialog picker;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Context context;
    @SuppressLint("ResourceType")
    public TimeWidget(Context context, LinearLayout screen, QuestionDef form) {
        this.screen = screen;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        this.context = context;
        /*TextView tv1 = new TextView(context);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, 0, 0, 0);
        tv1.setLayoutParams(layoutParams1);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        TimePicker time = new TimePicker(context);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        time.setLayoutParams(layoutParams);

        time.setScaleX((float) 0.6);
        time.setScaleY((float) 0.6);
        time.set
        time.setHour(hour);
        time.setMinute(minute);
        screen.addView(tv1);
        screen.addView(time);*/
        /**
         *
         * <LinearLayout
         *             android:layout_width="match_parent"
         *             android:layout_height="wrap_content"
         *             android:orientation="horizontal">
         *
         *             <TextView
         *                 android:id="@+id/textView5"
         *                 android:layout_width="wrap_content"
         *                 android:layout_height="wrap_content"
         *                 android:layout_weight="1"
         *                 android:text="TextView" />
         *
         *             <Button
         *                 android:id="@+id/button5"
         *                 android:layout_width="40px"
         *                 android:layout_height="100px"
         *                 android:layout_weight="1"
         *                 android:text="Button" />
         *         </LinearLayout>
         *
         */

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
    public LinearLayout getElement(){
        return screen;
    }
}
