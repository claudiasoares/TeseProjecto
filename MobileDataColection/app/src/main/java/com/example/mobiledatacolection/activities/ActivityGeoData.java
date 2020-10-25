package com.example.mobiledatacolection.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.example.mobiledatacolection.widget.interfaces.IGeoDataRequester;
import com.example.mobiledatacolection.widget.interfaces.WaitingForDataRegistry;

import org.javarosa.form.api.FormEntryPrompt;

public class ActivityGeoData implements IGeoDataRequester {

    private static final String LOCATION = "gp";
    private static final String ACCURACY_THRESHOLD = "accuracyThreshold";
    private static final String READ_ONLY = "readOnly";
    private static final String DRAGGABLE_ONLY = "draggable";

    @Override
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
        ((Activity) context).startActivityForResult(intent, ApplicationConstants.RequestCodes.LOCATION_CAPTURE);
    }

    @Override
    public void denied() {
    }
    }

    @Override
    public void requestGeoShape(Context context, FormEntryPrompt prompt, String answerText, WaitingForDataRegistry waitingForDataRegistry) {

    }

    @Override
    public void requestGeoTrace(Context context, FormEntryPrompt prompt, String answerText, WaitingForDataRegistry waitingForDataRegistry) {

    }
}