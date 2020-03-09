package com.example.mobiledatacolection.Widget;

import org.javarosa.form.api.FormEntryPrompt;

public class QuestionDetails {

    private final FormEntryPrompt prompt;
    private final String formAnalyticsID;

    public QuestionDetails(FormEntryPrompt prompt, String formAnalyticsID) {
        this.prompt = prompt;
        this.formAnalyticsID = formAnalyticsID;
    }

    public FormEntryPrompt getPrompt() {
        return prompt;
    }

    public String getFormAnalyticsID() {
        return formAnalyticsID;
    }
}
