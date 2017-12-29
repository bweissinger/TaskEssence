package com.madabysslabs.app.taskessence;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AppPreferences {
    private static final String KEY_PREFS_TASK_COLOR = "taskColor";
    private static final String KEY_PREFS_TASK_COMPLETED = "taskCompleted";
    private static final String KEY_PREFS_TASK = "task";
    private static final String KEY_PREFS_TASK_LIST_SIZE = "taskListSize";
    private static final String KEY_PREFS_TASKS_ENTERED = "tasksEntered";
    private static final String KEY_PREFS_TEMP_TASK = "tempTask";
    private static final String KEY_PREFS_TEMP_TASK_LIST_SIZE = "tempTaskListSize";
    private static final String KEY_PREFS_ALARM_TRIGGERED = "alarmIsTriggered";
    private static final String KEY_PREFS_ALL_TASKS_COMPLETED = "allTasksCompleted";
    private static final String KEY_PREFS_ALARM_DATE_TIME = "calendarDateTime";
    private static final String KEY_PREFS_SELECTED_RESET_TIME = "selectedResetTime";
    private static final String KEY_PREFS_CURRENT_RESET_TIME = "resetTime";
    private static final String KEY_PREFS_TASK_AVAILABLE = "taskAvailable";
    private static final String KEY_PREFS_NUM_TASKS_AVAILABLE = "numTasksAvailable";
    private static final String KEY_PREFS_TIMES_IN_ROW_FAILED_TASKS = "timesInRowFailedTasks";
    private static final String KEY_PREFS_TIMES_IN_ROW_COMPLETED_TASKS = "timesInRowCompletedTasks";
    private static final String KEY_PREFS_HELP_MENU_CURSOR_POSITION = "helpMenuCursorPosition";
    private static final String KEY_PREFS_USE_DARK_THEME = "useDarkTheme";
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public ArrayList<TaskItem> getTaskList() {
        ArrayList<TaskItem> listItems = new ArrayList<TaskItem>();
        int listSize = _sharedPrefs.getInt(KEY_PREFS_TASK_LIST_SIZE, 0);
        for (int i = 0; i < listSize; i++){
            String itemString = _sharedPrefs.getString(KEY_PREFS_TASK + i, "");
            int itemColorId = _sharedPrefs.getInt(KEY_PREFS_TASK_COLOR + i, 0);
            boolean isCompleted = _sharedPrefs.getBoolean(KEY_PREFS_TASK_COMPLETED + i, false);
            boolean isAvailable = _sharedPrefs.getBoolean(KEY_PREFS_TASK_AVAILABLE + i, true);

            listItems.add(new TaskItem(itemString, itemColorId, isCompleted, isAvailable));
        }
        return listItems;
    }

    public void saveTaskList(ArrayList<TaskItem> taskList) {
        _prefsEditor.putInt(KEY_PREFS_TASK_LIST_SIZE, taskList.size());
        Boolean allTasksCompleted = true;
        for(int i = 0; i < taskList.size(); i++){

            _prefsEditor.putString(KEY_PREFS_TASK + i, taskList.get(i).getTaskString());
            _prefsEditor.putInt(KEY_PREFS_TASK_COLOR + i, taskList.get(i).getColorId());
            _prefsEditor.putBoolean(KEY_PREFS_TASK_COMPLETED + i, taskList.get(i).isCompleted());
            _prefsEditor.putBoolean(KEY_PREFS_TASK_AVAILABLE + i, taskList.get(i).isAvailable());

            if(!taskList.get(i).isCompleted()){
                allTasksCompleted = false;
            }
        }
        if(allTasksCompleted){
            setTasksCompleted(true);
        }
        savePrefs();
    }

    public void setNumTasksAvailable(int numTasksAvailable){
        _prefsEditor.putInt(KEY_PREFS_NUM_TASKS_AVAILABLE, numTasksAvailable);
        savePrefs();
    }

    public int getNumTasksAvailable(){
        return _sharedPrefs.getInt(KEY_PREFS_NUM_TASKS_AVAILABLE, 3);
    }

    public void saveTempTaskList(ArrayList<String> tempTaskList){
        _prefsEditor.putInt(KEY_PREFS_TEMP_TASK_LIST_SIZE, tempTaskList.size());
        for(int i = 0; i < tempTaskList.size(); i++){
            _prefsEditor.putString(KEY_PREFS_TEMP_TASK + i, tempTaskList.get(i));
        }
        savePrefs();
    }

    public ArrayList<String> getTempTaskList(){
        ArrayList<String> tempTaskList = new ArrayList<String>();
        int tempTaskListSize = _sharedPrefs.getInt(KEY_PREFS_TEMP_TASK_LIST_SIZE, 0);
        for (int i = 0; i < tempTaskListSize; i++){
            tempTaskList.add(_sharedPrefs.getString(KEY_PREFS_TEMP_TASK + i, ""));
        }
        return tempTaskList;
    }

    public void setTimesInRowFailedTasks(int numTimesInRowFailedTasks){
        _prefsEditor.putInt(KEY_PREFS_TIMES_IN_ROW_FAILED_TASKS, numTimesInRowFailedTasks);
        savePrefs();
    }

    public int getTimesInRowFailedTasks(){
        return _sharedPrefs.getInt(KEY_PREFS_TIMES_IN_ROW_FAILED_TASKS, 0);
    }

    public void setTimesInRowCompletedTasks(int numTimesInRowCompletedTasks){
        _prefsEditor.putInt(KEY_PREFS_TIMES_IN_ROW_COMPLETED_TASKS, numTimesInRowCompletedTasks);
        savePrefs();
    }

    public int getTimesInRowCompletedTasks(){
        return _sharedPrefs.getInt(KEY_PREFS_TIMES_IN_ROW_COMPLETED_TASKS, 0);
    }

    private void savePrefs(){
        _prefsEditor.apply();
    }

    public void setTasksEntered(boolean tasksAreEntered){
        _prefsEditor.putBoolean(KEY_PREFS_TASKS_ENTERED, tasksAreEntered);
        savePrefs();
    }

    public boolean tasksAreEntered(){
        return _sharedPrefs.getBoolean(KEY_PREFS_TASKS_ENTERED, false);
    }

    public void setTasksCompleted(boolean tasksAreCompleted){
        _prefsEditor.putBoolean(KEY_PREFS_ALL_TASKS_COMPLETED, tasksAreCompleted);
        savePrefs();
    }

    public boolean tasksAreCompleted(){
        return _sharedPrefs.getBoolean(KEY_PREFS_ALL_TASKS_COMPLETED, false);
    }

    public void setAlarmDate(Calendar calendar){
        _prefsEditor.putLong(KEY_PREFS_ALARM_DATE_TIME, calendar.getTimeInMillis());
        savePrefs();
    }

    public Calendar getAlarmDate(){
        long timeInMillis = _sharedPrefs.getLong(KEY_PREFS_ALARM_DATE_TIME, 0);
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeInMillis);

        return calendar;
    }

    public Calendar getSelectedResetTime(){
        String resetTimeString = _sharedPrefs.getString(KEY_PREFS_SELECTED_RESET_TIME, "23:11");
        int hourOfDay = UtilityClass.parseHour(resetTimeString);
        int minute = UtilityClass.parseMinute(resetTimeString);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        return calendar;
    }

    public String getSelectedResetTimeString(){
        return _sharedPrefs.getString(KEY_PREFS_SELECTED_RESET_TIME, "23:11");
    }

    public void setResetTime(Calendar calendar){
        String time = (calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        _prefsEditor.putString(KEY_PREFS_CURRENT_RESET_TIME, time);
        _prefsEditor.apply();
    }

    public Calendar getResetTime(){
        String resetTimeString = _sharedPrefs.getString(KEY_PREFS_CURRENT_RESET_TIME, "23:11");
        int hourOfDay = UtilityClass.parseHour(resetTimeString);
        int minute = UtilityClass.parseMinute(resetTimeString);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        return calendar;
    }

    public String getResetTimeString() {
        return _sharedPrefs.getString(KEY_PREFS_CURRENT_RESET_TIME, "23:11");
    }

    public void setAlarmTriggered(boolean isTriggered){
        _prefsEditor.putBoolean(KEY_PREFS_ALARM_TRIGGERED, isTriggered);
        savePrefs();
    }

    public boolean alarmIsTriggered(){
        return _sharedPrefs.getBoolean(KEY_PREFS_ALARM_TRIGGERED, false);
    }

    public void setHelpMenuCursorPosition(int position){
        _prefsEditor.putInt(KEY_PREFS_HELP_MENU_CURSOR_POSITION, position);
        savePrefs();
    }

    public int getHelpMenuCursorPosition(){
        return _sharedPrefs.getInt(KEY_PREFS_HELP_MENU_CURSOR_POSITION, 0);
    }

    public void setUseDarkTheme(boolean isUsed){
        _prefsEditor.putBoolean(KEY_PREFS_USE_DARK_THEME, isUsed);
        savePrefs();
    }

    public boolean getUseDarkTheme(){
        return _sharedPrefs.getBoolean(KEY_PREFS_USE_DARK_THEME, false);
    }

}
