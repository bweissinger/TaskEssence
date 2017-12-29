package com.madabysslabs.app.taskessence;

import com.google.auto.value.AutoValue;

/**
 * Created by bryan on 7/22/17.
 */

@AutoValue
public abstract class TasksCompletedKey extends BaseKey{
    public static TasksCompletedKey create() {
        return new AutoValue_TasksCompletedKey();
    }

    @Override
    protected BaseFragment createFragment(){
        return new TasksCompleted();
    }

    @Override
    public AnimationContainer getAnimations(){
        return new AnimationContainer(
                R.anim.slide_in_from_right,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_left,
                R.anim.slide_out_to_right);
    }

    private static final String GROUP_TAG = "TaskLifecycleView";

    @Override
    public String getGroupTag() {
        return GROUP_TAG;
    }
}
