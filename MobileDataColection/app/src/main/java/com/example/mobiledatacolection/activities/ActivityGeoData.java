package com.example.mobiledatacolection.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.mobiledatacolection.widget.interfaces.*;
import com.example.mobiledatacolection.widget.utils.GeoWidgetUtils;

import org.javarosa.form.api.FormEntryPrompt;

public class ActivityGeoData  {

    private static final String LOCATION = "gp";
    private static final String ACCURACY_THRESHOLD = "accuracyThreshold";
    private static final String READ_ONLY = "readOnly";
    private static final String DRAGGABLE_ONLY = "draggable";

    public void requestGeoPoint(Context context, FormEntryPrompt prompt, String answerText, WaitingForDataRegistry waitingForDataRegistry) {
        Bundle bundle = new Bundle();

        if (answerText != null && !answerText.isEmpty()) {
            bundle.putDoubleArray(LOCATION, GeoWidgetUtils.getLocationParamsFromStringAnswer(answerText));
        }

        bundle.putDouble(ACCURACY_THRESHOLD, GeoWidgetUtils.getAccuracyThreshold(prompt.getQuestion()));
        bundle.putBoolean(READ_ONLY, prompt.isReadOnly());
        bundle.putBoolean(DRAGGABLE_ONLY, hasPlacementMapAppearance(prompt));

        Intent intent = new Intent(context, isMapsAppearance(prompt) ? GeoPointMapActivity.class : GeoPointActivity.class);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, 5);
    }

    private boolean hasPlacementMapAppearance(FormEntryPrompt prompt) {
        return false;
    }

    private boolean isMapsAppearance(FormEntryPrompt prompt) {
        return false;
    }


    public void denied() {
    }


}