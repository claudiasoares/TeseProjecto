package com.example.mobiledatacolection.widget.interfaces;

import org.javarosa.core.model.FormIndex;

public interface WaitingForDataRegistry {

        void waitForData(FormIndex index);

        boolean isWaitingForData(FormIndex index);

        void cancelWaitingForData();
}
