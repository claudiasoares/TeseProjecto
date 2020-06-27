package com.example.mobiledatacolection.widget;

import org.javarosa.form.api.FormEntryPrompt;

public class QuestionDetails {

    private final FormEntryPrompt prompt;
    public QuestionDetails(FormEntryPrompt prompt) {
        this.prompt = prompt;
    }

    public FormEntryPrompt getPrompt() {
        return prompt;
    }

}
