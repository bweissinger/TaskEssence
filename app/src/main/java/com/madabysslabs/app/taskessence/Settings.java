package com.madabysslabs.app.taskessence;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


public class Settings extends BaseFragment implements OnCustomSharedPreferenceChangeListener{

    public Settings() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        new SwitchPreference("useDarkTheme",
                PreferenceManager.getDefaultSharedPreferences(getContext()),
                rootView.findViewById(R.id.dark_theme_preference),
                "Dark theme",
                null,
                null,
                this);

        new TimePreference("selectedResetTime",
                PreferenceManager
                        .getDefaultSharedPreferences(getContext()),
                rootView.findViewById(R.id.new_tasks_reset_time_preference),
                "New tasks reset time",
                this);

        //Disable back button for this fragment
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
            appCompatActivity.setSupportActionBar(toolbar);
            if(((MainActivity) getActivity()).canGoBack()){
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        /*
        * Counter-intuitive, but must set true in order for onCreateOptionsMenu to be called
        * where the menu can then be removed by menu.clear
        * setHasOptionsMenu(false) will not remove the menu
        */
        setHasOptionsMenu(true);

        return rootView;
    }

    private void recreateActivity(){
        getActivity().recreate();
    }

    @Override
    public void onCustomSharedPreferenceChangeListener(SharedPreferences sharedPreferences, String preferenceKey) {

        switch (preferenceKey){
            case "selectedResetTime":
                break;

            case "useDarkTheme":
                recreateActivity();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Remove the menu from this fragment
        menu.clear();
    }
}
