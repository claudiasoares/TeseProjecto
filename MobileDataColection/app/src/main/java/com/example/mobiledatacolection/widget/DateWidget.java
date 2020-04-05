package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.javarosa.core.model.QuestionDef;

public class DateWidget
{
    private final LinearLayout screen;

    public DateWidget(Context context, LinearLayout screen, QuestionDef form) {
        this.screen = screen;
        screen.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutForInner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutForInner.setMargins(10,10,10,10);
        screen.setLayoutParams(layoutForInner);
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        DatePicker date = new DatePicker(context);

        screen.addView(tv1);
        screen.addView(date);
    }

    public LinearLayout getElement(){
        return screen;
    }
}
