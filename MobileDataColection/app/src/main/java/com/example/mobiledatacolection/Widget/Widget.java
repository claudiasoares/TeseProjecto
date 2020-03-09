package com.example.mobiledatacolection.Widget;

import org.javarosa.core.model.data.IAnswerData;

public interface Widget {

    IAnswerData getAnswer();

    void clearAnswer();

    void waitData();

    void cancelWaitingData();

    boolean isWaiting();
}
