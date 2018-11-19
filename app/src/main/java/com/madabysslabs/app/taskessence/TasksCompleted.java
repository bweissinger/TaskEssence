package com.madabysslabs.app.taskessence;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jaredrummler.android.widget.AnimatedSvgView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksCompleted extends BaseFragment {

    public TasksCompleted() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        initiateAnimatedSVG((AnimatedSvgView) getView().findViewById(R.id.animated_svg_view));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tasks_completed, container, false);

        //Disable back button for this fragment
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        //Set text displays for the view
        setTextView((TextView) view.findViewById(R.id.task_completed_text_view));
        setResetDateText((TextView) view.findViewById(R.id.task_completed_reset_date));

        // Inflate the layout for this fragment
        return view;
    }

    private void setResetDateText(TextView dateText){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, HH:mm");
        String date = dateFormat.format(appPreferences.getAlarmDate().getTime());

        dateText.setText("Reset: " + date);
    }

    private void setTextView(TextView textView){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        int numTimesInRowCompletedTasks = appPreferences.getTimesInRowCompletedTasks();

        //Unlike tasks not completed text view, the numTimesInRowCompletedTasks is set on the
        //previous screen.
        if(numTimesInRowCompletedTasks >= 3){
            textView.setText("You have gained a task.");
        }
        else{
            textView.setText("Excellent!");
        }
    }

    private void initiateAnimatedSVG(AnimatedSvgView svgView){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());

        //Get the task colors which will be set to the rocks' trace residue and fill color
        ArrayList<TaskItem> tasks = appPreferences.getTaskList();

        //Get references in order to get colors from theme
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();

        //Set trace color from the color specified in the theme
        theme.resolveAttribute(R.attr.colorIconTrace, typedValue, true);
        svgView.setTraceColor(typedValue.data);

        svgView.start();
    }

}
