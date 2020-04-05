package com.example.mobiledatacolection.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Selection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.R;

import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;

public class TextWidget{

  LinearLayout screen;
  @SuppressLint("ResourceAsColor")
  public TextWidget(Context context, LinearLayout screen, QuestionDef form) {
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
    EditText edq = new EditText(context);
    screen.addView(tv1);
    screen.addView(edq);
  }

  public LinearLayout getElement(){
    return screen;
  }

   /* public TextWidget(Context context,  QuestionDetails questionDetails, boolean readOnlyOverride) {
        super(context, questionDetails);
        readOnly = questionDetails.getPrompt().isReadOnly() || readOnlyOverride;
        answerText = getAnswerEditText(readOnly, getFormEntryPrompt());
        setUpLayout();
    }

    protected void setUpLayout() {
        setDisplayValueFromModel();
        addAnswerView(answerText);
    }



    @Override
    public void clearAnswer() {
        answerText.setText(null);
    }

    @Override
    public IAnswerData getAnswer() {
        String s = getAnswerText();
        return !s.equals("") ? new StringData(s) : null;
    }

    @NonNull
    public String getAnswerText() {
        return answerText.getText().toString();
    }



    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        answerText.setOnLongClickListener(l);
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        answerText.cancelLongPress();
    }



    public void setDisplayValueFromModel() {
        String currentAnswer = getFormEntryPrompt().getAnswerText();

        if (currentAnswer != null) {
            answerText.setText(currentAnswer);
            Selection.setSelection(answerText.getText(), answerText.getText().toString().length());
        }
    }*/
}