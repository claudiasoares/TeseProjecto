package com.example.mobiledatacolection.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.views.listeners.ChangeValueListener;
import com.example.mobiledatacolection.widget.QuestionWidget;
import com.example.mobiledatacolection.widget.interfaces.WidgetValueChangedListener;

import org.javarosa.form.api.FormEntryCaption;
import org.javarosa.form.api.FormEntryPrompt;

import static com.example.mobiledatacolection.dragger.DaggerUtils.getComponent;

public class MobileDataCollectView  extends FrameLayout implements View.OnLongClickListener, ChangeValueListener {
    private QuestionWidget widgetValueChangedListener;

    public MobileDataCollectView(@NonNull Context context, final FormEntryPrompt[] questionPrompts,
                                 FormEntryCaption[] groups, boolean advancingPage) {
        super(context);
        getComponent(context).inject(this);
        inflate(getContext(), R.layout.activity_menu, this);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void setWidgetValueChangedListener(WidgetValueChangedListener listener) {
       // widgetValueChangedListener = listener;
    }

    public void widgetValueChanged(QuestionWidget changedWidget) {
        if (widgetValueChangedListener != null) {
            widgetValueChangedListener.widgetValueChanged(changedWidget);
        }
    }
}
