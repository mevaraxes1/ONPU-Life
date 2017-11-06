package com.visorien.coursach.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "onpu_life_pref";

    public SharedPreferences getPref() {
        return pref;
    }

    private final SharedPreferences pref;

    public PreferencesHelper(Context context) {
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        pref.edit().clear().apply();
    }

}
