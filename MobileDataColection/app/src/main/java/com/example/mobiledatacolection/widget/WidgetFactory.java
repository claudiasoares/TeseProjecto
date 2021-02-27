package com.example.mobiledatacolection.widget;

import android.content.Context;

import com.example.mobiledatacolection.widget.interfaces.Widget;
import com.example.mobiledatacolection.widget.utils.QuestionDetails;

import org.javarosa.form.api.FormEntryPrompt;

import java.util.HashMap;

import static org.javarosa.core.model.Constants.*;

public class WidgetFactory {


    // CONTROL_INPUT -- contantsGetDataTypeInput
    private HashMap<Integer, Class> contantsDataTypeInput;
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



    private  HashMap<Integer, HashMap<Integer, Class>> contantsControlType;


    private  Context context;
    private QuestionDetails questionDetails;
    private  boolean readOnlyOverride;
    private HashMap<Integer, Class> contantsGetImageChoose;

    public WidgetFactory() {
       // questionDetails = new QuestionDetails(prompt, MobileDataCollect.getCurrentFormIdentifierHash());
        contantsControlType = new HashMap<Integer, HashMap<Integer, Class>>();


        contantsDataTypeInput = new HashMap<Integer, Class>();
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
        contantsGetImageChoose = new HashMap<Integer, Class>();
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
        contantsDataTypeInput.put(DATATYPE_DATE_TIME, DateTimeWidget.class);
        contantsDataTypeInput.put(DATATYPE_DATE, DateWidget.class);
        contantsDataTypeInput.put(DATATYPE_TIME, TimeWidget.class);
        contantsDataTypeInput.put(DATATYPE_DECIMAL, DecimalWidget.class);
        contantsDataTypeInput.put(DATATYPE_INTEGER, IntegerWidget.class);
        contantsDataTypeInput.put(DATATYPE_GEOPOINT, GeopointWidget.class);
        contantsDataTypeInput.put(DATATYPE_GEOSHAPE, GeoshapeWidget.class);
        contantsDataTypeInput.put(DATATYPE_GEOTRACE, GeotraceWidget.class);
        contantsDataTypeInput.put(DATATYPE_BARCODE, BarcodeWidget.class);
        contantsDataTypeInput.put(DATATYPE_TEXT, TextWidget.class);

    }

    private void populateDataFileCaptureInput() {
        contantsGetDataFileCapture.put(CONNECTION_NONE, FileUploadWidget.class);
    }

    private void populateDataTypeSelectOne() {
        contantsGetDataSelectOne.put(DATATYPE_CHOICE, SelectOneWidget.class);

    }
    private void populateDataTypeImageChoose() {
        contantsGetDataImageChoose.put(DATATYPE_CHOICE, SelectOneWidget.class);
        contantsGetDataImageChoose.put(DATATYPE_BINARY, ImageBinaryWidget.class);

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
        // contantsGetDataSelectMulti.put(DATATYPE_MULTIPLE_ITEMS, SelectMultiWidget.class);
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
        contantsControlType.put(CONTROL_INPUT, contantsDataTypeInput);
        contantsControlType.put(CONTROL_FILE_CAPTURE, contantsGetDataFileCapture );
        contantsControlType.put(CONTROL_IMAGE_CHOOSE, contantsGetDataImageChoose );
        contantsControlType.put(CONTROL_OSM_CAPTURE,contantsGetDataOmsCapture);
        contantsControlType.put(CONTROL_AUDIO_CAPTURE, contantsGetDataAudioCapture);
        contantsControlType.put(CONTROL_VIDEO_CAPTURE, contantsGetDataVideoCapture);
        contantsControlType.put(CONTROL_SELECT_ONE,contantsGetDataSelectOne);
        contantsControlType.put(CONTROL_SELECT_MULTI, contantsGetDataSelectMulti);
        contantsControlType.put(CONTROL_RANK, contantsGetDataRank);
        contantsControlType.put(CONTROL_TRIGGER, contantsGetDataTrigger);
        contantsControlType.put(CONTROL_RANGE,contantsGetDataRange );

    }
    public HashMap<Integer, HashMap<Integer, Class>> getHashMapControlType(){
        return contantsControlType;
    }

    public Widget createWidget(FormEntryPrompt prompt, Context context,
                               boolean readOnlyOverride) {

        // String appearance = WidgetAppearanceUtils.getSanitizedAppearanceHint(prompt);
        //QuestionDetails questionDetails = new QuestionDetails(prompt);

        // final QuestionWidget questionWidget;

        HashMap<Integer, Class> t = contantsControlType.get(prompt.getControlType());
        Class e = t.get(prompt.getDataType());
       // e.newInstance();
        return null;

    }
}
