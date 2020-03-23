package com.example.mobiledatacolection.widget.interfaces;

import org.javarosa.core.model.data.IAnswerData;

public interface Widget {

    IAnswerData getAnswer();

    void clearAnswer();

    void waitData();

    void cancelWaitingData();

    boolean isWaiting();
}
