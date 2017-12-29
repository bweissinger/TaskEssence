package com.madabysslabs.app.taskessence;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class EnterTasksView extends BaseFragment {

    private AppCompatEditText[] editTextList;
    private final int NUM_EDIT_TEXTS = 3;
    private ArrayList<TaskItem> taskItems;
    private Menu mMenu;
    private LinearLayout tasksEnterBackground;
    private Boolean confirmTasksIsVisible;

    public EnterTasksView() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.confirm_tasks:
                tasksEntered();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_enter_tasks_view, container, false);

        taskItems = new ArrayList<TaskItem>();
        referenceEditTextFields(rootView);
        checkEditTextFields();
        loadTempTaskList();

        //Background gets focus on click and hides keyboard
        tasksEnterBackground = (LinearLayout) rootView.findViewById(R.id.tasks_enter_view_background);
        tasksEnterBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksEnterBackground.requestFocus();
            }
        });
        tasksEnterBackground.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    hideKeyboard();
                }
            }
        });

        //Now set focus to background
        tasksEnterBackground.requestFocus();

        //Disable back button for this fragment
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void referenceEditTextFields(View rootView){

        //Get context of root view
        Context context = rootView.getContext();

        //Find the package name for the application
        String packageName = context.getPackageName();

        //Get app prefs to use with num tasks available
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());

        //Reference edit text fields
        //Add detection for when text is entered into field
        editTextList = new AppCompatEditText[NUM_EDIT_TEXTS];
        for (int i = 0; i < NUM_EDIT_TEXTS; i++){
            editTextList[i] = (AppCompatEditText) rootView.findViewById(
                    context.getResources()
                            .getIdentifier(
                                    "editText" + (i + 1),
                                    "id",
                                    packageName)
            );

            editTextList[i].addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    //Do nothing
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    //Do nothing
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (!TextUtils.isEmpty(s) && !confirmTasksIsVisible) {
                        enableConfirmTasks(true);
                    }
                    else if(TextUtils.isEmpty(s)){
                        checkEditTextFields();
                    }
                }
            });
        }

        int timesInRowCompletedTasks = appPreferences.getTimesInRowCompletedTasks();
        int timesInRowFailedTasks = appPreferences.getTimesInRowFailedTasks();
        int numTasksAvailable = appPreferences.getNumTasksAvailable();

        if(timesInRowCompletedTasks >= 3){
            if(numTasksAvailable < 3){
                numTasksAvailable++;
                appPreferences.setNumTasksAvailable(numTasksAvailable);
            }
            appPreferences.setTimesInRowCompletedTasks(0);
        }
        if(timesInRowFailedTasks >= 2){
            if(numTasksAvailable > 1){
                numTasksAvailable--;
                appPreferences.setNumTasksAvailable(numTasksAvailable);
            }
            appPreferences.setTimesInRowFailedTasks(0);
        }

        //Now set tasks availability
        for(int i = 0; i < editTextList.length; i++){
            if (numTasksAvailable <= 0) {
                editTextList[i].setEnabled(false);
                editTextList[i].setHint("You have lost this task.");
            }
            else {
                editTextList[i].setEnabled(true);
                editTextList[i].setHint("Enter " +
                        UtilityClass.convertWholeNumberToPlace(i + 1, false) +
                        " task.");
            }
            numTasksAvailable--;
        }
    }

    private void enableConfirmTasks(Boolean setVisible){
        confirmTasksIsVisible = setVisible;
        if (mMenu != null && mMenu.findItem(R.id.confirm_tasks) != null) {
            mMenu.findItem(R.id.confirm_tasks).setVisible(setVisible);
        }
    }

    private void checkEditTextFields(){
        Boolean editTextIsEmpty = true;
        for (int i = 0; i < editTextList.length; i++) {
            String iterString = editTextList[i].getText().toString();
            if (!TextUtils.isEmpty(iterString)) {
                editTextIsEmpty = false;
            }
        }
        if (editTextIsEmpty) {
            enableConfirmTasks(false);
        }
        else{
            enableConfirmTasks(true);
        }
    }

    private void setTaskList(){
        TaskColors colorPicker = new TaskColors();
        int[] colors = colorPicker.getTaskColors(editTextList.length);
        for (int i = 0; i < editTextList.length; i++) {
            String iterString = editTextList[i].getText().toString();
            if (!TextUtils.isEmpty(iterString)) {
                taskItems.add(new TaskItem(iterString, colors[i], false, editTextList[i].isEnabled()));
            }
            else{
                //Determine if user did not input a task or if it was not available
                if(editTextList[i].isEnabled()){
                    taskItems.add(new TaskItem("Task not added.", colors[i], true, editTextList[i].isEnabled()));
                }
                else{
                    taskItems.add(new TaskItem("You have lost this task.", colors[i], true, editTextList[i].isEnabled()));
                }
            }
        }
    }

    private void hideKeyboard(){
        //Get activity and hide keyboard
        Activity act = getActivity();
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = act.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_enter_tasks_view, menu);
        mMenu = menu;
        checkEditTextFields();
    }

    @Override
    public void onResume(){
        super.onResume();

        loadTempTaskList();
        checkEditTextFields();
    }

    private void loadTempTaskList(){
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());

        ArrayList<String> tempTaskList = appPreferences.getTempTaskList();
        for(int i = 0; i < tempTaskList.size() && i < editTextList.length; i++){
            editTextList[i].setText(tempTaskList.get(i));
        }
        checkEditTextFields();
    }

    @Override
    public void onPause(){
        super.onPause();

        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        if(!appPreferences.tasksAreEntered()){

            //Create temp task list and get text fields and save to shared prefs
            ArrayList<String> tempTaskList= new ArrayList<String>();

            for (int i = 0; i < editTextList.length; i++) {
                String iterString = editTextList[i].getText().toString();
                tempTaskList.add(i, iterString);
            }

            appPreferences.saveTempTaskList(tempTaskList);
        }
    }

    private void setAlarm(AppPreferences appPreferences){
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        Calendar selectedResetTime = appPreferences.getSelectedResetTime();
        Calendar currentResetTime = appPreferences.getResetTime();

        calSet.set(Calendar.HOUR_OF_DAY, currentResetTime.get(Calendar.HOUR_OF_DAY));
        calSet.set(Calendar.MINUTE, currentResetTime.get(Calendar.MINUTE));

        if(!selectedResetTime.equals(currentResetTime)){
            if(selectedResetTime.after(currentResetTime)){
                calSet.set(Calendar.HOUR_OF_DAY, selectedResetTime.get(Calendar.HOUR_OF_DAY));
                calSet.set(Calendar.MINUTE, selectedResetTime.get(Calendar.MINUTE));
            }
            else{
                /*If the selected reset time is earlier than the current reset time, than the reset time
                is set to the next passing of that time. This is because the reset time has to have already
                passed.
                 */
                calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, selectedResetTime.get(Calendar.HOUR_OF_DAY));
                calSet.set(Calendar.MINUTE, selectedResetTime.get(Calendar.MINUTE));

                //Sets alarm one day ahead if time has already passed
                if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }
            }
            //Now change the reset time to the time that was selected
            appPreferences.setResetTime(selectedResetTime);
        }

        MyAlarm myAlarm = new MyAlarm();
        myAlarm.setAlarm(this.getContext(), calSet);
    }

    private void tasksEntered(){
        //Null temp task list in app prefs
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        ArrayList<String> tempTaskList = appPreferences.getTempTaskList();
        for(int i = 0; i < tempTaskList.size(); i++){
            tempTaskList.set(i, "");
        }
        appPreferences.saveTempTaskList(tempTaskList);

        //Set and save task list, then continue to Current Tasks View fragment
        setTaskList();
        appPreferences.saveTaskList(taskItems);
        appPreferences.setTasksEntered(true);

        setAlarm(appPreferences);

        MainActivity.get(getContext()).replaceHistoryFirstInstanceOfGroup(CurrentTasksViewKey.create());
    }

}
