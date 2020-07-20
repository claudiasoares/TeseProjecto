package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobiledatacolection.widget.utils.WidgetUtils;
import com.google.firebase.database.DatabaseReference;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryPrompt;

import static com.example.mobiledatacolection.widget.utils.WidgetUtils.createSimpleButton;
import static com.example.mobiledatacolection.widget.utils.WidgetUtils.getCenteredAnswerTextView;

public class BarcodeWidget {

    // private final Button barcodeButton;
    // private final TextView stringAnswer;
    private final LinearLayout screen;
    private final DatabaseReference databaseReference;

    public BarcodeWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt formEntryPrompt, int version, DatabaseReference databaseReference){
        this.screen = screen;
        this.databaseReference = databaseReference;
        screen.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutForInner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutForInner.setMargins(10,10,10,10);
        screen.setLayoutParams(layoutForInner);
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();


        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);





        screen.addView(tv1);



/*

        barcodeButton = createSimpleButton(context, formEntryPrompt.isReadOnly(), context.getString(R.string.get_barcode), getAnswerFontSize(), this);

        stringAnswer = getCenteredAnswerTextView(context, getAnswerFontSize());

        String s = questionDetails.getPrompt().getAnswerText();
        if (s != null) {
            barcodeButton.setText(context.getString(
                    R.string.replace_barcode));
            stringAnswer.setText(s);
        }
        // finish complex layout
        LinearLayout answerLayout = new LinearLayout(context);
        answerLayout.setOrientation(LinearLayout.VERTICAL);
        answerLayout.addView(barcodeButton);
        answerLayout.addView(stringAnswer);
        addAnswerView(answerLayout, WidgetUtils.getStandardMargin(context));*/
    }


}
