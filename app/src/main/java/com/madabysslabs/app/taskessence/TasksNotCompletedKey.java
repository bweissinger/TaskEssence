package com.madabysslabs.app.taskessence;

import com.google.auto.value.AutoValue;

/**
 * Created by bryan on 7/22/17.
 */

@AutoValue
public abstract class TasksNotCompletedKey extends BaseKey{
    public static TasksNotCompletedKey create() {
        return new AutoValue_TasksNotCompletedKey();
    }

    @Override
    protected BaseFragment createFragment(){
        return new TasksNotCompleted();
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
