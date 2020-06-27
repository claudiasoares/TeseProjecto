package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.widget.LinearLayout;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

import static java.security.AccessController.getContext;

public class GeopointWidget{
    private final Context context;
    private final LinearLayout linearlayout;
    private final QuestionDef questionDef;
    public static final String LOCATION = "gp";
    public static final String ACCURACY_THRESHOLD = "accuracyThreshold";
    public static final String READ_ONLY = "readOnly";
    public static final String DRAGGABLE_ONLY = "draggable";

    public static final double DEFAULT_LOCATION_ACCURACY = 5.0;
    private double accuracyThreshold;

    public GeopointWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version) {
      //  determineMapProperties();
        this.questionDef = form;
        this.linearlayout = screen;
        this.context = context;
    }

  /*  private void determineMapProperties() {
        // Determine the accuracy threshold to use.
        String acc = questionDef.getAdditionalAttribute(null, ACCURACY_THRESHOLD);
        accuracyThreshold = acc != null && !acc.isEmpty() ? Double.parseDouble(acc) : DEFAULT_LOCATION_ACCURACY;

        // Determine whether to use the map and whether the point should be draggable.
        if (MapProvider.getConfigurator().isAvailable(getContext())) {
            if (hasAppearance(getFormEntryPrompt(), PLACEMENT_MAP)) {
                draggable = true;
                useMap = true;
            } else if (hasAppearance(getFormEntryPrompt(), MAPS)) {
                draggable = false;
                useMap = true;
            }
        }
    }

    public void updateButtonLabelsAndVisibility(boolean dataAvailable) {
        if (useMap) {
            if (readOnly) {
                startGeoButton.setText(R.string.geopoint_view_read_only);
            } else {
                startGeoButton.setText(
                        dataAvailable ? R.string.view_change_location : R.string.get_point);
            }
        } else {
            if (!readOnly) {
                startGeoButton.setText(
                        dataAvailable ? R.string.change_location : R.string.get_point);
            }
        }
    }*/
}
