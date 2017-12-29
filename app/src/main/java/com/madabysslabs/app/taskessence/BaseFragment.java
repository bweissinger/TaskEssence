package com.madabysslabs.app.taskessence;

import android.support.v4.app.Fragment;

/**
 * Created by bryan on 7/19/17.
 */

public class BaseFragment extends Fragment{
    public final <T extends BaseKey> T getKey(){
        return getArguments() != null ? getArguments().<T>getParcelable("KEY") : null;
    }
}
