package com.example.mobiledatacolection.tasks;

import org.javarosa.core.model.FormDef;

public class IFormLoader {
    public void onProgressStep(String value) {
    }

    public <ExternalDataManager> void loadingComplete(FormLoaderTask externalDataManagerFormLoaderTask, FormDef formDef) {
    }

    public void loadingError(String errorMsg) {

    }
}
