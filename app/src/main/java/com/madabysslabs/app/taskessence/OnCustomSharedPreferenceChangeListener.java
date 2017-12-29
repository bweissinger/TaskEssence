package com.madabysslabs.app.taskessence;

import android.content.SharedPreferences;

/**
 * Created by bryan on 7/21/17.
 */

public interface OnCustomSharedPreferenceChangeListener {
    void onCustomSharedPreferenceChangeListener(SharedPreferences sharedPreferences, String preferenceKey);
}
