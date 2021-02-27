package com.example.mobiledatacolection.widget.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.example.mobiledatacolection.R;

import static android.view.View.GONE;

public class WidgetUtils {
    private static final int WIDGET_ANSWER_STANDARD_MARGIN_MODIFIER = 4;

    private WidgetUtils() {

    }

    public static int getStandardMargin(Context context) {
        Resources resources = context.getResources();
        int marginStandard = dpFromPx(context, resources.getDimensionPixelSize(R.dimen.margin_standard));

        return marginStandard - WIDGET_ANSWER_STANDARD_MARGIN_MODIFIER;
    }

    public static TextView getCenteredAnswerTextView(Context context, int answerFontSize) {
        TextView textView = createAnswerTextView(context, answerFontSize);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    public static TextView createAnswerTextView(Context context, int answerFontSize) {
        return createAnswerTextView(context, "", answerFontSize);
    }

    public static TextView createAnswerTextView(Context context, String text, int answerFontSize) {
        TextView textView = new TextView(context);


        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, answerFontSize);
        textView.setPadding(20, 20, 20, 20);
        textView.setText(text);

        return textView;
    }

    public static ImageView createAnswerImageView(Context context, Bitmap bitmap) {
        final ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setTag("ImageView");
        imageView.setPadding(10, 10, 10, 10);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    public static Button createSimpleButton(Context context, @IdRes final int withId, boolean readOnly, String text, int answerFontSize, QuestionWidget listener) {
        final Button button = new Button(context);

        if (readOnly) {
            button.setVisibility(GONE);
        } else {
            button.setId(withId);
            button.setText(text);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, answerFontSize);
            button.setPadding(20, 20, 20, 20);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.setMargins(7, 5, 7, 5);

            button.setLayoutParams(params);

            button.setOnClickListener(v -> {

            });
        }

        return button;
    }

    public static Button createSimpleButton(Context context,  boolean readOnly, int answerFontSize, QuestionWidget listener) {
        return createSimpleButton(context,  readOnly, null, answerFontSize, listener);
    }

    public static Button createSimpleButton(Context context, boolean readOnly, String text, int answerFontSize, QuestionWidget listener) {
        return createSimpleButton(context,  readOnly, text, answerFontSize, listener);
    }

    public static int dpFromPx(final Context context, final float px) {
        return Math.round(px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(final Context context, final float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}
