package com.madabysslabs.app.taskessence;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by bryan on 7/21/17.
 */

public class SwitchPreference{

    private final SharedPreferences sharedPreferences;
    private final String preferenceKey;

    public SwitchPreference(@NonNull String preferenceKey,
                            @NonNull SharedPreferences sharedPreferences,
                            @NonNull View switchPreferenceLayout,
                            @NonNull String titleText,
                            @Nullable String summaryOn,
                            @Nullable String summaryOff,
                            @Nullable final OnCustomSharedPreferenceChangeListener listener){

        this.preferenceKey = preferenceKey;
        this.sharedPreferences = sharedPreferences;

        initPreference(switchPreferenceLayout,
                titleText,
                summaryOn,
                summaryOff,
                listener);
    }

    private void initPreference(View switchPreferenceLayout,
                                String titleText,
                                final String summaryOn,
                                final String summaryOff,
                                final OnCustomSharedPreferenceChangeListener listener){

        //Set switch preference title
        ((TextView) switchPreferenceLayout
                .findViewById(R.id.settings_item_switch_preference_label))
                .setText(titleText);

        //Get summary text view to pass to switchToggled inside of onClick, set null and invisible if no summary text given
        final TextView summary;
        if(summaryOn == null || summaryOff == null) {
            switchPreferenceLayout
                    .findViewById(R.id.settings_item_switch_preference_summary)
                    .setVisibility(View.GONE);
            summary = null;
        }
        else {
            summary = (TextView) switchPreferenceLayout
                    .findViewById(R.id.settings_item_switch_preference_summary);
        }

        //Find the actual switch of the switch preference
        final Switch prefSwitch = (Switch) switchPreferenceLayout.findViewById(R.id.settings_item_switch_preference_switch);

        //Init preference view
        prefSwitch.setChecked(sharedPreferences.getBoolean(preferenceKey, false));
        switchToggled(prefSwitch, summary, summaryOn, summaryOff);

        //Set onClick listener for the main layout
        switchPreferenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefSwitch.setChecked(!sharedPreferences.getBoolean(preferenceKey, false));
                switchToggled(prefSwitch, summary, summaryOn, summaryOff);
                if(listener != null){
                    listener.onCustomSharedPreferenceChangeListener(sharedPreferences, preferenceKey);
                }
            }
        });

        //Set onClick listener for the switch
        prefSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToggled(prefSwitch, summary, summaryOn, summaryOff);
                if(listener != null){
                    listener.onCustomSharedPreferenceChangeListener(sharedPreferences, preferenceKey);
                }
            }
        });

    }

    private void switchToggled(Switch prefSwitch, TextView summary, String summaryOn, String summaryOff){
        if(prefSwitch.isChecked()){
            sharedPreferences.edit().putBoolean(preferenceKey, true).commit();
            if(summary != null) {summary.setText(summaryOn);}
        }
        else{
            sharedPreferences.edit().putBoolean(preferenceKey, false).commit();
            if(summary != null) {summary.setText(summaryOff);}
        }
    }

}
