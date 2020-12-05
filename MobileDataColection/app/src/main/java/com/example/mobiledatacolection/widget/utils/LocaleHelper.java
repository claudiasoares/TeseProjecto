package com.example.mobiledatacolection.widget.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.mobiledatacolection.MobileDataCollect;


import java.util.Locale;
import java.util.TreeMap;

/**
 * Changes the locale of the app and keeps the changes persistent
 *
 * @author abdulwd
 */
public class LocaleHelper {
    public static String getLocaleCode(Context context) {
        String localeCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("PT", "");
        boolean isUsingSysLanguage = localeCode.equals("");
        if (isUsingSysLanguage) {
            localeCode = MobileDataCollect.defaultSysLanguage;
        }
        return localeCode;
    }

    public TreeMap<String, String> getEntryListValues() {
        //Holds language as key and language code as value
        TreeMap<String, String> languageList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            languageList.put("pt", "pt");

        return languageList;
    }

    public Locale getLocale(Context context) {
        return getLocale(getLocaleCode(context));
    }

    private Locale getLocale(String splitLocaleCode) {
        if (splitLocaleCode.contains("_")) {
            String[] arg = splitLocaleCode.split("_");
            return new Locale(arg[0], arg[1]);
        } else {
            return new Locale(splitLocaleCode);
        }
    }
}
