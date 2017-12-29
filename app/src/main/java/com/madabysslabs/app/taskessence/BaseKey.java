package com.madabysslabs.app.taskessence;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by bryan on 7/19/17.
 */

public abstract class BaseKey implements Parcelable{

    public String getFragmentTag(){
        return getClass().getName();
    }

    public final BaseFragment newFragment(){
        BaseFragment fragment = createFragment();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("KEY", this);
        fragment.setArguments(bundle);
        return fragment;
    }

    public abstract String getGroupTag();

    public abstract AnimationContainer getAnimations();

    protected abstract BaseFragment createFragment();
}
