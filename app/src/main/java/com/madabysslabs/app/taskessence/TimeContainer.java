package com.madabysslabs.app.taskessence;

/**
 * Created by bryan on 7/21/17.
 */

public class TimeContainer {
    public final int hour;
    public final int minute;
    public final String formattedTime;

    public TimeContainer(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
        formattedTime = UtilityClass.timeToString(hour, minute);
    }

    public TimeContainer(String formattedTime){
        this.formattedTime = formattedTime;
        hour = UtilityClass.parseHour(formattedTime);
        minute = UtilityClass.parseMinute(formattedTime);
    }
}
