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

import java.util.ArrayList;


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
        textView.setText("You have failed.");
    }

    private void initiateAnimatedSVG(AnimatedSvgView svgView){
        //Get references in order to get colors from theme
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();

        //Set trace color from the color specified in the theme
        theme.resolveAttribute(R.attr.colorIconTrace, typedValue, true);
        svgView.setTraceColor(typedValue.data);

        svgView.start();
    }

    private void setCompletedTasksStrings(){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        ArrayList<TaskItem> tasks = appPreferences.getTaskList();

        for (int i = 0; i < tasks.size(); i++){
            if(tasks.get(i).isCompleted()){
                tasks.set(i, tasks.set(i, new TaskItem("", tasks.get(i).getColorId(), true, true)));
            }
        }

        appPreferences.saveTaskList(tasks);
    }

    private void goToEnterTasksView(){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());

        //Reset status of tasks
        appPreferences.setTasksEntered(false);
        appPreferences.setTasksCompleted(false);
        setCompletedTasksStrings();

        MainActivity.get(getContext()).replaceHistoryFirstInstanceOfGroup(EnterTasksViewKey.create());
    }

}
