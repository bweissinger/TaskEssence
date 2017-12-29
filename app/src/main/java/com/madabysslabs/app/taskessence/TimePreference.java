package com.madabysslabs.app.taskessence;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePreference implements TimePickerDialog.OnTimeSetListener
{
    private final String preferenceKey;
    private final SharedPreferences sharedPreferences;
    private final OnCustomSharedPreferenceChangeListener listener;
    private final View timePreferenceLayout;

    public TimePreference(@NonNull String preferenceKey,
                          @NonNull SharedPreferences sharedPreferences,
                          @NonNull View timePreferenceLayout,
                          @NonNull String titleText,
                          @Nullable final OnCustomSharedPreferenceChangeListener listener){

        this.preferenceKey = preferenceKey;
        this.sharedPreferences = sharedPreferences;
        this.listener = listener;
        this.timePreferenceLayout = timePreferenceLayout;

        initTimePreference(timePreferenceLayout, titleText);
    }

    public TimeContainer getTime(){
        return new TimeContainer(sharedPreferences.getString(preferenceKey, "00:00"));
    }

    private void initTimePreference(final View timePreferenceLayout, String titleText){

        //Set switch preference title
        ((TextView) timePreferenceLayout
                .findViewById(R.id.settings_item_time_preference_label))
                .setText(titleText);

        setSummary(getTime().formattedTime);

        final TimePickerDialog.OnTimeSetListener timePickerListener = this;
        //Set onClick listener for the main layout
        timePreferenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeContainer timeContainer = getTime();
                new TimePickerDialog(timePreferenceLayout.getContext(),
                        getTheme(timePreferenceLayout.getContext()),
                        timePickerListener,
                        timeContainer.hour,
                        timeContainer.minute,
                        true)
                        .show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimeContainer time = new TimeContainer(hourOfDay, minute);
        sharedPreferences.edit().putString(preferenceKey, time.formattedTime).commit();
        setSummary(time.formattedTime);
        if(listener != null){
            listener.onCustomSharedPreferenceChangeListener(sharedPreferences, preferenceKey);
        }
    }

    private void setSummary(String summary){
        ((TextView)timePreferenceLayout.findViewById(R.id.settings_item_time_preference_summary)).setText(summary);
    }

    private int getTheme(Context context){
        AppPreferences appPreferences = new AppPreferences(context);

        if(appPreferences.getUseDarkTheme()){
            return R.style.TimePickerDark;
        }

        return R.style.TimePickerLight;
    }
}
