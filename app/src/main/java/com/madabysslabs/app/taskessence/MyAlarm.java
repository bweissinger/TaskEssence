package com.madabysslabs.app.taskessence;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Bryan on 3/5/2017.
 */

public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Use a alarm triggered flag just in case it fires a little bit early
        //the app will still switch screens if the user is in the app
        AppPreferences appPreferences = new AppPreferences(context);
        appPreferences.setAlarmTriggered(true);

        //Send message to be received by MainActivity's broadcastReceiver
        context.sendBroadcast(new Intent("CHECK_STATE"));
    }

    public void setAlarm(Context context, Calendar calendar) {

        //Save alarm date to shared prefs
        AppPreferences appPrefs = new AppPreferences(context);
        appPrefs.setAlarmDate(calendar);
        System.out.println(calendar);
        appPrefs.setAlarmTriggered(false);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
                pendingIntent);
    }
}

