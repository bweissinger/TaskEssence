package com.madabysslabs.app.taskessence;

public class TaskItem {

    private String mTaskString;
    private int mColorId;
    private boolean mIsAvailable;
    private boolean mIsCompleted;

    public TaskItem(String taskString, int colorId, boolean isCompleted, boolean isAvailable){
        mTaskString = taskString;
        mColorId = colorId;
        mIsCompleted = isCompleted;
        mIsAvailable = isAvailable;
    }

    public String getTaskString(){
        return mTaskString;
    }

    public int getColorId(){
        return mColorId;
    }

    public boolean isAvailable(){ return mIsAvailable; }

    public boolean isCompleted(){ return mIsCompleted; }

    public void setCompleted(){ mIsCompleted = true; }

}
