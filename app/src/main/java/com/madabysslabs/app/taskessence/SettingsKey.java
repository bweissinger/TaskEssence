package com.madabysslabs.app.taskessence;

import com.google.auto.value.AutoValue;

/**
 * Created by bryan on 7/20/17.
 */

@AutoValue
public abstract class SettingsKey extends BaseKey{
    public static SettingsKey create() {
        return new AutoValue_SettingsKey();
    }

    @Override
    protected BaseFragment createFragment(){
        return new Settings();
    }

    @Override
    public AnimationContainer getAnimations(){
        return new AnimationContainer(
                R.anim.slide_in_from_right,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_left,
                R.anim.slide_out_to_right);
    }

    private static final String GROUP_TAG = "Settings";

    @Override
    public String getGroupTag() {
        return GROUP_TAG;
    }
}
