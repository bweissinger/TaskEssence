package com.madabysslabs.app.taskessence;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

        //Fetch task list
        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        taskItems = appPreferences.getTaskList();

        referenceEditTextFields(rootView);

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

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {}

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    setConfirmButtonVisibility();
                }
            });
        }
    }

    private int dpToPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (16*scale + 0.5f);
    }

    private void setTextFields(){
        for(int i = 0; i < editTextList.length; i++){
            String taskString = taskItems.get(i).getTaskString();
            editTextList[i].setEnabled(true);
            if (!taskItems.get(i).isCompleted()){
                editTextList[i].setEnabled(false);
                editTextList[i].setHint(taskItems.get(i).getTaskString());
                TextInputLayout layout = (TextInputLayout) ((ViewGroup) editTextList[i].getParent()).getParent();
                layout.setCounterEnabled(false);
                layout.setPadding(layout.getPaddingLeft(), layout.getPaddingTop(), layout.getPaddingRight(), dpToPx(16));
            }
            else if (taskString.equals("")){
                editTextList[i].setEnabled(true);
                editTextList[i].setHint("Enter " +
                        UtilityClass.convertWholeNumberToPlace(i + 1, false) +
                        " task.");
            }
            else {
                editTextList[i].setText(taskString);
            }
        }
    }

    private void enableConfirmTasks(Boolean setVisible){
        if (mMenu != null && mMenu.findItem(R.id.confirm_tasks) != null) {
            mMenu.findItem(R.id.confirm_tasks).setVisible(setVisible);
        }
    }

    private void setConfirmButtonVisibility(){
        for (AppCompatEditText editText : editTextList){
            if (!editText.getText().toString().isEmpty() || !editText.isEnabled()){
                enableConfirmTasks(true);
                return;
            }
        }
        enableConfirmTasks(false);
    }

    private void setTaskList(){
        TaskColors colorPicker = new TaskColors();
        int[] colors = colorPicker.getTaskColors(taskItems.size());
        for (int i = 0; i < taskItems.size(); i++) {
            String iterString = editTextList[i].getText().toString();
            if (!TextUtils.isEmpty(iterString)) {
                taskItems.set(i, new TaskItem(iterString, colors[i], false, true));
            }
            else if (taskItems.get(i).isCompleted()){
                //User did not input a task
                taskItems.set(i, new TaskItem("Task not added.", colors[i], true, true));
            }
            else{
                taskItems.set(i, new TaskItem(taskItems.get(i).getTaskString(), colors[i], false, true));
            }
        }
    }

    private void setTaskStringsFromTextFields(){
        for (int i = 0; i < taskItems.size(); i++){
            TaskItem taskItem = taskItems.get(i);
            if (taskItem.isCompleted()){
                taskItems.set(i,
                        new TaskItem(editTextList[i].getText().toString(),
                                taskItem.getColorId(),
                                true,
                                true));
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
        setConfirmButtonVisibility();
    }

    @Override
    public void onResume(){
        super.onResume();

        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        taskItems = appPreferences.getTaskList();

        setTextFields();
    }

    @Override
    public void onPause(){
        super.onPause();

        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        if (!appPreferences.tasksAreEntered()){
            setTaskStringsFromTextFields();
        }
        appPreferences.saveTaskList(taskItems);
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
            }

            //Now change the reset time to the time that was selected
            appPreferences.setResetTime(selectedResetTime);
        }

        //Sets alarm one day ahead if time has already passed
        if (calSet.before(calNow)) {
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        System.out.println("Set: " + calSet.getTime().toString());
        System.out.println("Date: " + calNow.getTime().toString());


        MyAlarm myAlarm = new MyAlarm();
        myAlarm.setAlarm(this.getContext(), calSet);
    }

    private void tasksEntered(){
        //Set and save task list, then continue to Current Tasks View fragment
        setTaskList();

        AppPreferences appPreferences = new AppPreferences(getActivity().getApplicationContext());
        appPreferences.saveTaskList(taskItems);
        appPreferences.setTasksEntered(true);

        setAlarm(appPreferences);

        MainActivity.get(getContext()).replaceHistoryFirstInstanceOfGroup(CurrentTasksViewKey.create());
    }

}
