package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

public class GeoshapeWidget {
    private final DatabaseReference databaseReference;

    public GeoshapeWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version, DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}
