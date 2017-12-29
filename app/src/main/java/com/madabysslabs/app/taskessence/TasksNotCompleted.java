package com.madabysslabs.app.taskessence;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jaredrummler.android.widget.AnimatedSvgView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksNotCompleted extends BaseFragment {


    public TasksNotCompleted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tasks_not_completed, container, false);

        //Disable back button for this fragment
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Find and start the svg animation
        initiateAnimatedSVG((AnimatedSvgView) getView().findViewById(R.id.animated_svg_view_tasks_not_completed));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add on click listener to entire screen
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.tasks_not_completed_frame_layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEnterTasksView();
            }
        });

        //Set text to indicate if a task has been removed
        TextView textView = (TextView) view.findViewById(R.id.task_not_completed_text_view);
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        int numTimesInRowFailedTasks = appPreferences.getTimesInRowFailedTasks();

        /*
        *numTimesInRowFailedTasks text prompts logic is set to trigger at one less than normal,
        *at 1 instead of 2, because it isn't increased until the user navigates away from the screen
        */
        if(numTimesInRowFailedTasks >= 1){
            textView.setText("You have lost a task.");
        }
        else{
            textView.setText("You have failed.");
        }
    }

    private void initiateAnimatedSVG(AnimatedSvgView svgView){
        //Get references in order to get colors from theme
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();

        theme.resolveAttribute(R.attr.colorFlowerTrace, typedValue, true);
        svgView.setTraceColor(typedValue.data);

        svgView.start();
    }

    private void goToEnterTasksView(){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());

        //Reset status of tasks
        appPreferences.setTasksEntered(false);
        appPreferences.setTasksCompleted(false);

        //reset task completed streak and add day to tasks not completed streak
        appPreferences.setTimesInRowFailedTasks((appPreferences.getTimesInRowFailedTasks() + 1));
        appPreferences.setTimesInRowCompletedTasks(0);

        MainActivity.get(getContext()).replaceHistoryFirstInstanceOfGroup(EnterTasksViewKey.create());
    }

}
