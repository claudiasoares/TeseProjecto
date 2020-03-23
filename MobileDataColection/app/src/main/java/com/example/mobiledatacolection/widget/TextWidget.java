package com.example.mobiledatacolection.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Selection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.MobileDataCollect;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;

@SuppressLint("ViewConstructor")
public class TextWidget extends  QuestionWidget{

    boolean readOnly;
    protected final EditText answerText;


    public TextWidget(Context context,  QuestionDetails questionDetails, boolean readOnlyOverride) {
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
    }
}