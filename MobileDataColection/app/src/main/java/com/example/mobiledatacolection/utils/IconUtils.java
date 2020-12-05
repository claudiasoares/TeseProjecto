package com.example.mobiledatacolection.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.dto.Instance;

public class IconUtils {

    private IconUtils() {
    }

    public static int getNotificationAppIcon() {
        return R.drawable.ic_notes_white;
    }

    /** Renders a Drawable (such as a vector drawable) into a Bitmap. */
    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {  // shortcut if it's already a bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                return bitmap;
            }
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {  // negative if Drawable is a solid colour
            width = height = 1;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable getSubmissionSummaryStatusIcon(Context context, String instanceStatus) {
        switch (instanceStatus) {
            case "incomplete":
                return ContextCompat.getDrawable(context, R.drawable.form_state_saved);
            case "complete":
                return ContextCompat.getDrawable(context, R.drawable.form_state_finalized);
            case "submitted":
                return ContextCompat.getDrawable(context, R.drawable.form_state_submited);
            case "submissionFailed":
                return ContextCompat.getDrawable(context, R.drawable.form_state_submission_failed);
        }
        return null;
    }
}
