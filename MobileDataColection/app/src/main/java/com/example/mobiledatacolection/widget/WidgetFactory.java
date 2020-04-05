package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.os.Build;
import com.example.mobiledatacolection.widget.interfaces.Widget;

import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;

import static org.javarosa.core.model.Constants.*;

public class WidgetFactory {



    private  HashMap<Integer, HashMap<Integer, Class>> contantsGetControlType;
    private HashMap<Integer, Class> contantsGetDataTypeInput;

    private  Context context;
    private  QuestionDetails questionDetails;
    private  boolean readOnlyOverride;

    public WidgetFactory() {
       // questionDetails = new QuestionDetails(prompt, MobileDataCollect.getCurrentFormIdentifierHash());
        contantsGetControlType = new HashMap<Integer, HashMap<Integer, Class>>();
        contantsGetDataTypeInput = new HashMap<Integer, Class>();
        populateDataTypeControlInput();
        populateControlType();


    }

    private void populateDataTypeControlInput() {
        contantsGetDataTypeInput.put(DATATYPE_DATE_TIME, DateTimeWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_DATE, DateWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_TIME, TimeWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_DECIMAL, DecimalWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_INTEGER, IntegerWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_GEOPOINT, GeopointWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_GEOSHAPE, GeoshapeWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_GEOTRACE, GeotraceWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_BARCODE, BarcodeWidget.class);
        contantsGetDataTypeInput.put(DATATYPE_TEXT, TextWidget.class);
    }

    private void populateControlType() {
        contantsGetControlType.put(CONTROL_INPUT, contantsGetDataTypeInput);
        //contantsGetControlType.put(CONTROL_FILE_CAPTURE, Arb);
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
    public HashMap<Integer, HashMap<Integer, Class>> getHashMapControlType(){
        return contantsGetControlType;
    }

    public Widget createWidget(FormEntryPrompt prompt, Context context,
                               boolean readOnlyOverride) {

        // String appearance = WidgetAppearanceUtils.getSanitizedAppearanceHint(prompt);
        //QuestionDetails questionDetails = new QuestionDetails(prompt);

        // final QuestionWidget questionWidget;

        HashMap<Integer, Class> t = contantsGetControlType.get(prompt.getControlType());
        Class e = t.get(prompt.getDataType());
       // e.newInstance();
        return null;

    }
}
