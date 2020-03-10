package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.widget.RelativeLayout;

import org.javarosa.core.model.data.IAnswerData;

public class QuestionWidget extends RelativeLayout
        implements Widget {
    public QuestionWidget(Context context) {
        super(context);
    }

    @Override
    public IAnswerData getAnswer() {
        return null;
    }

    @Override
    public void clearAnswer() {

    }

    @Override
    public void waitData() {

    }

    @Override
    public void cancelWaitingData() {

    }

    @Override
    public boolean isWaiting() {
        return false;
    }
}
