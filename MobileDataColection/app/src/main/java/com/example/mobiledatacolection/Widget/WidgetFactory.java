package com.example.mobiledatacolection.Widget;

import android.content.Context;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.Widget.Widget;

import org.javarosa.core.model.Constants;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;

import static org.javarosa.core.model.Constants.*;

public class WidgetFactory {


    HashMap<Integer, HashMap<Integer,  Class<? extends Widget>>> contantsGetControlType;
    HashMap<Integer,  Class<? extends Widget>> contantsGetDataTypeControlInput;
    private WidgetFactory() {
        contantsGetControlType=new HashMap<Integer, HashMap<Integer,  Class<? extends Widget>>>();
        contantsGetDataTypeControlInput = new HashMap<Integer,  Class<? extends Widget>>();


    }

    private void populateDataTypeControlInput(){
        contantsGetDataTypeControlInput.put(DATATYPE_DATE_TIME, \


        contantsGetDataTypeControlInput.put(DATATYPE_TIME, TimeWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_DECIMAL, DecimalWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_INTEGER, IntegerWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_GEOPOINT, GeopointWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_GEOSHAPE, GeoshapeWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_GEOTRACE, GeotraceWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_BARCODE, BarcodeWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_TEXT, TextWidget.class);
        contantsGetDataTypeControlInput.put(DATATYPE_BOOLEAN, BooleanWidget.class);
    }

    private void populateControlType(){
        contantsGetControlType.put(CONTROL_INPUT, contantsGetDataTypeControlInput );
        contantsGetControlType.put(CONTROL_FILE_CAPTURE,);
        contantsGetControlType.put(CONTROL_IMAGE_CHOOSE,);
        contantsGetControlType.put(CONTROL_AUDIO_CAPTURE,);
        contantsGetControlType.put(CONTROL_OSM_CAPTURE,);
        contantsGetControlType.put(CONTROL_VIDEO_CAPTURE,);
        contantsGetControlType.put(CONTROL_SELECT_ONE,);
        contantsGetControlType.put(CONTROL_SELECT_MULTI,);
        contantsGetControlType.put(CONTROL_RANK,);
        contantsGetControlType.put(CONTROL_TRIGGER,);
        contantsGetControlType.put(CONTROL_RANGE,);

    }

    public Widget createWidget(FormEntryPrompt prompt, Context context,
                                      boolean readOnlyOverride) {

       // String appearance = WidgetAppearanceUtils.getSanitizedAppearanceHint(prompt);
        QuestionDetails questionDetails = new QuestionDetails(prompt);

       // final QuestionWidget questionWidget;

        HashMap<Integer,  Class<? extends Widget>> t = contantsGetControlType.get(prompt.getControlType());
        Class<? extends Widget> e = t.get(prompt.getDataType());

        questionWidget = new DateTimeWidget(context, questionDetails);
        switch (prompt.getControlType()) {
            case Constants.CONTROL_INPUT:
                switch (prompt.getDataType()) {
                    case Constants.DATATYPE_DATE_TIME:
                        break;
                    case Constants.DATATYPE_DATE:
                        break;
                    case Constants.DATATYPE_TIME:
                        break;
                    case Constants.DATATYPE_DECIMAL:
                        break;
                    case DATATYPE_INTEGER:
                        break;
                    case Constants.DATATYPE_GEOPOINT:
                        break;
                    case Constants.DATATYPE_GEOSHAPE:
                        break;
                    case Constants.DATATYPE_GEOTRACE:
                        break;
                    case Constants.DATATYPE_BARCODE:
                        break;
                    case Constants.DATATYPE_TEXT:

                        break;
                    case Constants.DATATYPE_BOOLEAN:

                        break;
                    default:

                        break;
                }
                break;
            case Constants.CONTROL_FILE_CAPTURE:
                break;
            case Constants.CONTROL_IMAGE_CHOOSE:
                break;
            case Constants.CONTROL_OSM_CAPTURE:
                break;
            case Constants.CONTROL_AUDIO_CAPTURE:
                break;
            case Constants.CONTROL_VIDEO_CAPTURE:
                break;
            case Constants.CONTROL_SELECT_ONE:
                break;
            case Constants.CONTROL_SELECT_MULTI:
                break;
            case Constants.CONTROL_RANK:
                break;
            case Constants.CONTROL_TRIGGER:
                break;
            case Constants.CONTROL_RANGE:

                break;
            default:

                break;
        }

        return null;
    }
}
