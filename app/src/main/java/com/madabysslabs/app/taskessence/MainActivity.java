package com.madabysslabs.app.taskessence;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zhuinden.simplestack.BackstackDelegate;
import com.zhuinden.simplestack.HistoryBuilder;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;

import java.util.List;



public class MainActivity extends AppCompatActivity implements StateChanger{

    private static final String TAG = "MAINACTIVITY";

    //Holds size of (new) backstack during a transaction
    StateChangeInformation currentStateChange = new StateChangeInformation();

    BackstackDelegate backstackDelegate;
    FragmentStateChanger fragmentStateChanger;
    Bundle onSavedInstanceBundle;
    private final IntentFilter intentFilter = new IntentFilter("CHECK_STATE");

    //This is used to check the state of the tasks after the alarm goes off
    //Should only do this when the app is currently up and being used
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            determineState();
        }
    };

    //Unregister the broacastReceiver when app goes to the background
    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    //Register broadcastReceiver when app goes to foreground
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        determineState();
        // If onSavedInstanceBundle != null, then android will restore backstack information
        if(onSavedInstanceBundle == null){
            //determineState();
        }
    }

    private void determineTheme(){
        AppPreferences appPreferences = new AppPreferences(this);
        if(appPreferences.getUseDarkTheme()){
            setTheme(R.style.BaseAppTheme_Dark_NoActionBar);
        }
        else{
            setTheme(R.style.BaseAppTheme_Light_NoActionBar);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Theme must be set before view is set
        determineTheme();

        //Set up simplestack backstack manager
        backstackDelegate = new BackstackDelegate(null);
        backstackDelegate.onCreate(savedInstanceState,
                getLastCustomNonConfigurationInstance(),
                HistoryBuilder.single(EnterTasksViewKey.create()));
        backstackDelegate.registerForLifecycleCallbacks(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onSavedInstanceBundle = savedInstanceState;
        fragmentStateChanger = new FragmentStateChanger(getSupportFragmentManager(), R.id.activity_main);
        backstackDelegate.setStateChanger(this);
    }

    public Boolean canGoBack(){

        //Use the size of the new stack if a state change is in progress, otherwise use the current stack
        if(currentStateChange.stateChangeIsActive()){
            return currentStateChange.getNewBackstackSize() > 1;
        }
        return backstackDelegate.getBackstack().getHistory().size() > 1;

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return backstackDelegate.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public void onBackPressed() {
        if (!backstackDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void replaceHistory(Object rootKey){
        backstackDelegate.getBackstack()
                .setHistory(HistoryBuilder.single(rootKey),
                        StateChange.REPLACE);
    }

    public void replaceHistoryFirstInstanceOfGroup(BaseKey newKey){
        if(backstackDelegate.getBackstack().getHistory().size() > 0){
            List<Object> history = HistoryHelper.replaceFirstInstanceOfGroupTag(backstackDelegate
                            .getBackstack()
                            .getHistory(),
                    newKey);
            backstackDelegate
                    .getBackstack()
                    .setHistory(history, StateChange.REPLACE);
        }
        else{
            navigateTo(newKey);
        }
    }

    public void navigateTo(Object key){
        backstackDelegate.getBackstack().goTo(key);
    }

    public static MainActivity get(Context context){
        // noinspection ResourceType
        return (MainActivity)context.getSystemService(TAG);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if(TAG.equals(name)) {
            return this;
        }
        return super.getSystemService(name);
    }

    @Override
    public void handleStateChange(@NonNull StateChange stateChange, @NonNull Callback completionCallback) {

        currentStateChange.setNewStateChangeInProgress(stateChange);

        //Only handle fragment transaction if new fragment is different from the viewable fragment
        if(stateChange.topNewState().equals(stateChange.topPreviousState())){
            completionCallback.stateChangeComplete();
            return;
        }
        fragmentStateChanger.handleStateChange(stateChange);
        completionCallback.stateChangeComplete();
        currentStateChange.setStateChangeComplete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                navigateTo(SettingsKey.create());
                return true;

            case R.id.action_rate:
                rate();
                return true;

            case R.id.action_share:
                share();
                return true;

            case R.id.action_help_and_feedback:
                navigateTo(HelpAndFeedbackKey.create());
                return true;

            case android.R.id.home:
                backstackDelegate.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void rate(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            UtilityClass.showErrorAlertDialog(this, "Rate", "Failed to launch the market");
        }
    }

    private void share(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Task Essence");
            String sAux = "\nTry out this great To-Do app!\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            UtilityClass.showErrorAlertDialog(this, "Share", "Failed to share");
        }
    }

    private boolean timeIsRemaining(){
        AppPreferences appPrefs = new AppPreferences(this);

        /* Check to see if the alarm is triggered first.
        if it is not, then check the date. The reason for
        this is that the alarm manager is not always precise
        to the minute. It is redundancy just in case the
        alarm manager doesn't trip, or is late.
         */
        if(appPrefs.alarmIsTriggered()){
            return false;
        }
        else{
            java.util.Calendar current = new java.util.GregorianCalendar();
            java.util.Calendar alarm = appPrefs.getAlarmDate();
            if(current.equals(alarm) || current.after(alarm)){
                return false;
            }
        }

        return true;
    }

    private void determineState(){

        AppPreferences appPreferences = new AppPreferences(this);

        if(appPreferences.tasksAreEntered()){
            if(!appPreferences.tasksAreCompleted()){
                if(timeIsRemaining()){
                    //Go to current tasks screen
                    replaceHistoryFirstInstanceOfGroup(CurrentTasksViewKey.create());
                }
                else{
                    //Go to tasks not completed screen
                    replaceHistoryFirstInstanceOfGroup(TasksNotCompletedKey.create());
                }
            }
            else{
                if(timeIsRemaining()){
                    //Go to tasks completed screen
                    replaceHistoryFirstInstanceOfGroup(TasksCompletedKey.create());
                }
                else{
                    //Reset status in app preferences then continue to Enter Tasks View
                    appPreferences.setTasksEntered(false);
                    appPreferences.setTasksCompleted(false);
                    replaceHistoryFirstInstanceOfGroup(EnterTasksViewKey.create());
                }
            }
        }
        else{
            //Go to enter tasks screen
            replaceHistoryFirstInstanceOfGroup(EnterTasksViewKey.create());
        }
    }

}



