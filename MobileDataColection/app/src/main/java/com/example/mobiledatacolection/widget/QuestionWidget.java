package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.utils.Themes;
import com.example.mobiledatacolection.widget.interfaces.Widget;
import com.example.mobiledatacolection.widget.interfaces.WidgetValueChangedListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.form.api.FormEntryPrompt;


import timber.log.Timber;

import static com.example.mobiledatacolection.dragger.DaggerUtils.getComponent;

public abstract class QuestionWidget extends RelativeLayout
        implements Widget {
    private final Themes theme;
    private final int questionFontSize;
    private final QuestionDetails questionDetails;
    private final FormEntryPrompt formEntryPrompt;
    private WidgetValueChangedListener valueChangedListener;
    public QuestionWidget(Context context, QuestionDetails questionDetails) {
        super(context);
        getComponent(context).inject(this);

        theme = new Themes(context);

        questionFontSize = MobileDataCollect.getQuestionFontsize();
        this.questionDetails = questionDetails;
        formEntryPrompt = questionDetails.getPrompt();

        setGravity(Gravity.TOP);
        setPadding(0, 7, 0, 0);
    }

    @Override
    public IAnswerData getAnswer() {
        return null;
    }

    @Override
    public void clearAnswer() {

    }

    @Override
    public void waitData() {

    }

    @Override
    public void cancelWaitingData() {

    }

    @Override
    public boolean isWaiting() {
        return false;
    }


    public FormEntryPrompt getFormEntryPrompt() {
        return formEntryPrompt;
    }

    public QuestionDetails getQuestionDetails() {
        return questionDetails;
    }


    protected void addAnswerView(View v) {
        if (v == null) {
            Timber.e("cannot add a null view as an answerView");
            return;
        }
        // default place to add answer
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);


        params.setMargins(10, 0, 10, 0);
        addView(v, params);
    }

    protected EditText getAnswerEditText(boolean readOnly, FormEntryPrompt prompt) {
        EditText answerEditText = new EditText(getContext());
        int id = 0;

            id = View.generateViewId();

        answerEditText.setId(id);
        answerEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, questionFontSize);
        answerEditText.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.SENTENCES, false));

        // needed to make long read only text scroll
        answerEditText.setHorizontallyScrolling(false);
        answerEditText.setSingleLine(false);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        params.setMargins(7, 5, 7, 5);
        answerEditText.setLayoutParams(params);

        if (readOnly) {
            answerEditText.setBackground(null);
            answerEditText.setEnabled(false);
            answerEditText.setTextColor(theme.getPrimaryTextColor());
            answerEditText.setFocusable(false);
        }

        answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // widgetValueChanged(changedWidget);
            }
        });



        QuestionDef questionDef = prompt.getQuestion();
        if (questionDef != null) {
            /*
             * If a 'rows' attribute is on the input tag, set the minimum number of lines
             * to display in the field to that value.
             *
             * I.e.,
             * <input ref="foo" rows="5">
             *   ...
             * </input>
             *
             * will set the height of the EditText box to 5 rows high.
             */
            String height = questionDef.getAdditionalAttribute(null, "rows");
            if (height != null && height.length() != 0) {
                try {
                    int rows = Integer.parseInt(height);
                    answerEditText.setMinLines(rows);
                    answerEditText.setGravity(Gravity.TOP); // to write test starting at the top of the edit area
                } catch (Exception e) {
                    Timber.e("Unable to process the rows setting for the answerText field: %s", e.toString());
                }
            }
        }

        return answerEditText;
    }

    public void widgetValueChanged(QuestionWidget changedWidget) {
        if (valueChangedListener != null) {
            valueChangedListener.widgetValueChanged(this);
        }
    }

}
