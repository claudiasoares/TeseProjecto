package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.javarosa.core.model.QuestionDef;

public class DecimalWidget {
    private LinearLayout screen;

    public DecimalWidget(Context context, LinearLayout screen, QuestionDef form) {
        this.screen = screen;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        EditText edq = new EditText(context);
        edq.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        screen.addView(tv1);
        screen.addView(edq);
    }

    public LinearLayout getElement(){
        return screen;
    }
}
