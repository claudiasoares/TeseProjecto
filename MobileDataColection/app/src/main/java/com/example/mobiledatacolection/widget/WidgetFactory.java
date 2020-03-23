package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.widget.interfaces.Widget;

import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;

import static org.javarosa.core.model.Constants.*;

public class WidgetFactory {


    private final HashMap<Integer, QuestionWidget> contantsGetDataTypeControlFileCapture;
    private final HashMap<Integer, HashMap<Integer, QuestionWidget>> contantsGetControlType;
    private final HashMap<Integer, QuestionWidget> contantsGetDataTypeControlInput;
    private final Context context;
    private final QuestionDetails questionDetails;
    private final boolean readOnlyOverride;

    private WidgetFactory(Context context, FormEntryPrompt prompt,  boolean readOnlyOverride) {
        questionDetails = new QuestionDetails(prompt, MobileDataCollect.getCurrentFormIdentifierHash());
        contantsGetControlType = new HashMap<Integer, HashMap<Integer, QuestionWidget>>();
        contantsGetDataTypeControlInput = new HashMap<Integer, QuestionWidget>();
        contantsGetDataTypeControlFileCapture = new HashMap<Integer, QuestionWidget>();
        this.readOnlyOverride = readOnlyOverride;
        this.context = context;
        populateDataTypeControlInput();
        populateControlType();


    }

    private void populateDataTypeControlInput() {
        contantsGetDataTypeControlInput.put(DATATYPE_DATE_TIME, new DateTimeWidget(context, questionDetails ));
        contantsGetDataTypeControlInput.put(DATATYPE_TIME, new TimeWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_DECIMAL, new DecimalWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_INTEGER, new IntegerWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_GEOPOINT, new GeopointWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_GEOSHAPE, new GeoshapeWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_GEOTRACE, new GeotraceWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_BARCODE, new BarcodeWidget(context, questionDetails));
        contantsGetDataTypeControlInput.put(DATATYPE_TEXT, new TextWidget(context, questionDetails, readOnlyOverride ));
        contantsGetDataTypeControlInput.put(DATATYPE_BOOLEAN, new BooleanWidget(context, questionDetails));
    }

    private void populateControlType() {
        contantsGetControlType.put(CONTROL_INPUT, contantsGetDataTypeControlInput);
        contantsGetControlType.put(CONTROL_FILE_CAPTURE, contantsGetDataTypeControlFileCapture);
       /* contantsGetControlType.put(CONTROL_IMAGE_CHOOSE, );
        contantsGetControlType.put(CONTROL_AUDIO_CAPTURE, );
        contantsGetControlType.put(CONTROL_OSM_CAPTURE, );
        contantsGetControlType.put(CONTROL_VIDEO_CAPTURE, );
        contantsGetControlType.put(CONTROL_SELECT_ONE, );
        contantsGetControlType.put(CONTROL_SELECT_MULTI, );
        contantsGetControlType.put(CONTROL_RANK, );
        contantsGetControlType.put(CONTROL_TRIGGER, );
        contantsGetControlType.put(CONTROL_RANGE, );*/

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Widget createWidget(FormEntryPrompt prompt, Context context,
                               boolean readOnlyOverride) {

        // String appearance = WidgetAppearanceUtils.getSanitizedAppearanceHint(prompt);
        //QuestionDetails questionDetails = new QuestionDetails(prompt);

        // final QuestionWidget questionWidget;

        HashMap<Integer, QuestionWidget> t = contantsGetControlType.get(prompt.getControlType());
        QuestionWidget e = t.get(prompt.getDataType());
        return e;

    }
}
