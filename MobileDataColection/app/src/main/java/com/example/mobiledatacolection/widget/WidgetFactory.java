package com.example.mobiledatacolection.widget;

import android.content.Context;

import com.example.mobiledatacolection.widget.interfaces.Widget;

import org.javarosa.core.model.data.SelectMultiData;
import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;

import static org.javarosa.core.model.Constants.*;

public class WidgetFactory {


    // CONTROL_INPUT -- contantsGetDataTypeInput
    private HashMap<Integer, Class> contantsGetDataTypeInput;
    // CONTROL_FILE_CAPTURE contantsGetDataFileCapture
    private HashMap<Integer, Class> contantsGetDataFileCapture;
    // CONTROL_IMAGE_CHOOSE, contantsGetDataImageChoose
    private HashMap<Integer, Class> contantsGetDataImageChoose;
    // CONTROL_OSM_CAPTURE,contantsGetDataOmsCapture
    private HashMap<Integer, Class> contantsGetDataOmsCapture;
    // CONTROL_AUDIO_CAPTURE, contantsGetDataAudioCapture
    private HashMap<Integer, Class> contantsGetDataAudioCapture;
    // CONTROL_VIDEO_CAPTURE, contantsGetDataVideoCapture
    private HashMap<Integer, Class> contantsGetDataVideoCapture;
    // CONTROL_SELECT_ONE,contantsGetDataSelectOne
    private HashMap<Integer, Class> contantsGetDataSelectOne;
    // CONTROL_SELECT_MULTI, contantsGetDataSelectMulti
    private HashMap<Integer, Class> contantsGetDataSelectMulti;
    // CONTROL_RANK, contantsGetDataRank
    private HashMap<Integer, Class> contantsGetDataRank;
    // CONTROL_TRIGGER, contantsGetDataTrigger
    private HashMap<Integer, Class> contantsGetDataTrigger;
    // CONTROL_RANGE,contantsGetDataRange
    private HashMap<Integer, Class> contantsGetDataRange;



    private  HashMap<Integer, HashMap<Integer, Class>> contantsGetControlType;


    private  Context context;
    private  QuestionDetails questionDetails;
    private  boolean readOnlyOverride;

    public WidgetFactory() {
       // questionDetails = new QuestionDetails(prompt, MobileDataCollect.getCurrentFormIdentifierHash());
        contantsGetControlType = new HashMap<Integer, HashMap<Integer, Class>>();


        contantsGetDataTypeInput = new HashMap<Integer, Class>();
        contantsGetDataSelectOne = new HashMap<Integer, Class>();
        contantsGetDataFileCapture = new HashMap<Integer, Class>();
        contantsGetDataImageChoose = new HashMap<Integer, Class>();
        contantsGetDataOmsCapture = new HashMap<Integer, Class>();
        contantsGetDataAudioCapture = new HashMap<Integer, Class>();
        contantsGetDataVideoCapture = new HashMap<Integer, Class>();
        contantsGetDataSelectMulti = new HashMap<Integer, Class>();
        contantsGetDataRank = new HashMap<Integer, Class>();
        contantsGetDataTrigger = new HashMap<Integer, Class>();
        contantsGetDataRange = new HashMap<Integer, Class>();
        populateDataTypeControlInput();
        populateDataFileCaptureInput();
        populateControlType();
        populateDataTypeSelectOne();
        populateDataTypeRange();
        populateDataTypeImageChoose();
        populateDataTypeOmsCapture();
        populateDataTypeAudioCapture();
        populateDataTypeVideoCapture();
        populateDataTypeSelectMultiple();
        populateDataTypeRank();
        populateDataTypeTrigger();

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

    private void populateDataFileCaptureInput() {
        contantsGetDataFileCapture.put(CONNECTION_NONE, FileUploadWidget.class);
    }

    private void populateDataTypeSelectOne() {
        contantsGetDataSelectOne.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeImageChoose() {
        contantsGetDataImageChoose.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeOmsCapture() {
        contantsGetDataOmsCapture.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeAudioCapture() {
        contantsGetDataAudioCapture.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeVideoCapture() {
        contantsGetDataAudioCapture.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeSelectMultiple() {
        contantsGetDataSelectMulti.put(DATATYPE_CHOICE, SelectMultiWidget.class);
        contantsGetDataSelectMulti.put(DATATYPE_MULTIPLE_ITEMS, MultipleItemsWidget.class);


    }
    private void populateDataTypeRank() {
        contantsGetDataRank.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeTrigger() {
        contantsGetDataTrigger.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeRange() {
        contantsGetDataRange.put(DATATYPE_INTEGER, IntegerWidget.class);
        contantsGetDataRange.put(DATATYPE_DECIMAL, DecimalWidget.class);

    }


    private void populateControlType() {
        contantsGetControlType.put(CONTROL_INPUT, contantsGetDataTypeInput);
        contantsGetControlType.put(CONTROL_FILE_CAPTURE, contantsGetDataFileCapture );
        contantsGetControlType.put(CONTROL_IMAGE_CHOOSE, contantsGetDataImageChoose );
        contantsGetControlType.put(CONTROL_OSM_CAPTURE,contantsGetDataOmsCapture);
        contantsGetControlType.put(CONTROL_AUDIO_CAPTURE, contantsGetDataAudioCapture);
        contantsGetControlType.put(CONTROL_VIDEO_CAPTURE, contantsGetDataVideoCapture);
        contantsGetControlType.put(CONTROL_SELECT_ONE,contantsGetDataSelectOne);
        contantsGetControlType.put(CONTROL_SELECT_MULTI, contantsGetDataSelectMulti);
        contantsGetControlType.put(CONTROL_RANK, contantsGetDataRank);
        contantsGetControlType.put(CONTROL_TRIGGER, contantsGetDataTrigger);
        contantsGetControlType.put(CONTROL_RANGE,contantsGetDataRange );

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
