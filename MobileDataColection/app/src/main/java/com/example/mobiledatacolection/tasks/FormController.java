package com.example.mobiledatacolection.tasks;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.services.PropertyManager;
import org.javarosa.form.api.FormEntryController;

import java.io.File;

public class FormController {
    public FormController(File formMediaDir, FormEntryController fec, Object o) {
    }

    public static void initializeJavaRosa(PropertyManager mgr) {
    }

    public FormIndex getIndexFromXPath(String xpath) {
        return  null;
    }

    public void jumpToIndex(FormIndex idx) {
    }

    public void setIndexWaitingForData(FormIndex idx) {
    }

    public FormDef getFormDef() {
        return null;
    }

    public String getFormTitle() {
        return null;
    }
}
