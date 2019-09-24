package com.madabysslabs.app.taskessence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class CurrentTasksView extends BaseFragment {

    private ArrayList<TaskItem> taskItems;
    private ArrayList<TaskCard> cards;

    public CurrentTasksView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_current_tasks_view, container, false);

        //Get saved task list
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        taskItems = appPreferences.getTaskList();
        cards = new ArrayList<TaskCard>();
        initTaskCards(rootView);

        //Disable back button for this fragment
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return rootView;
    }

    private void initTaskCards(View rootView){
        //create new card items in ArrayList
        for(int i = 0; i < taskItems.size(); i++){
            cards.add(new TaskCard
                    (this,
                    rootView,
                    i + 1,
                    ContextCompat.getColor(getContext(), taskItems.get(i).getColorId()),
                    taskItems.get(i).getTaskString(),
                    taskItems.get(i).isCompleted(),
                    getActivity().getTheme()));
        }
    }

    private void allTasksCompleted(){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        appPreferences.setTasksCompleted(true);

        MainActivity.get(getContext()).replaceHistoryFirstInstanceOfGroup(TasksCompletedKey.create());
    }

    public void taskCompleted(int taskNumber){
        //Set task as completed
        taskItems.get(taskNumber - 1).setCompleted();

        //Save task list
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        appPreferences.saveTaskList(taskItems);

        //Now check to see if all tasks are completed
        if(appPreferences.tasksAreCompleted()){
            allTasksCompleted();
        }
    }

}
