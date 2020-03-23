package com.example.mobiledatacolection.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;


import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.R;

import static com.example.mobiledatacolection.preferences.GeneralKeys.KEY_APP_THEME;

public class Themes {

    private final Context context;

    public Themes(Context context) {
        this.context = context;
    }

    @StyleRes
    public int getAppTheme() {
        return isDarkTheme() ? R.style.Theme_MobileDataCollect_Dark : R.style.Theme_MobileDataCollect_Light;
    }

    @StyleRes
    public int getFormEntryActivityTheme() {
        return isDarkTheme() ? R.style.Theme_MobileDataCollect_Activity_FormEntryActivity_Dark : R.style.Theme_MobileDataCollect_Activity_FormEntryActivity_Light;
    }

    @StyleRes
    public int getSettingsTheme() {
        return isDarkTheme() ? R.style.Theme_MobileDataCollect_Settings_Dark : R.style.Theme_MobileDataCollect_Settings_Light;
    }

    @StyleRes
    public int getBottomDialogTheme() {
        return isDarkTheme() ? R.style.Theme_MobileDataCollect_MaterialDialogSheet_Dark : R.style.Theme_MobileDataCollect_MaterialDialogSheet_Light;
    }

    @DrawableRes
    public int getDivider() {
        return isDarkTheme() ? android.R.drawable.divider_horizontal_dark : android.R.drawable.divider_horizontal_bright;
    }

    public boolean isHoloDialogTheme(int theme) {
        return theme == android.R.style.Theme_Holo_Light_Dialog ||
                theme == android.R.style.Theme_Holo_Dialog;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @StyleRes
    public int getMaterialDialogTheme() {
        return isDarkTheme() ?
                android.R.style.Theme_Material_Dialog :
                android.R.style.Theme_Material_Light_Dialog;
    }

    @StyleRes
    public int getHoloDialogTheme() {
        return isDarkTheme() ?
                android.R.style.Theme_Holo_Dialog :
                android.R.style.Theme_Holo_Light_Dialog;
    }

    private int getAttributeValue(@AttrRes int resId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, outValue, true);
        return outValue.data;
    }

    private int getColorValue(@ColorInt int resId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, outValue, true);
        return outValue.data;
    }

    public int getAccountPickerTheme() {
        return isDarkTheme() ? 0 : 1;
    }

    public boolean isDarkTheme() {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_APP_THEME, 0); // 0 - for private mode

        return preferences.equals(context.getString(R.string.app_theme_dark));
    }

    /**
     * @return Text color for the current {@link android.content.res.Resources.Theme}
     *
     * @deprecated use {@link #getColorOnSurface()} instead
     */
    @ColorInt
    @Deprecated
    public int getPrimaryTextColor() {
        return context.getResources().getColor(R.color.primaryTextColor);
    }

    /**
     * @return Text color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getColorOnSurface() {
        return getAttributeValue(R.attr.colorOnSurface);
    }

    /**
     * @return Accent color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getAccentColor() {
        return getAttributeValue(R.attr.colorAccent);
    }

    /**
     * @return Icon color for the current {@link android.content.res.Resources.Theme}
     */
    @ColorInt
    public int getIconColor() {
        return context.getResources().getColor(R.color.iconColor);
    }


}
