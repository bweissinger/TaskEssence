package com.madabysslabs.app.taskessence;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by bryan on 7/23/17.
 */

public class HistoryHelper {

    /*
     * The method first places the contents of the list into an array, then checks for the tag.
     * If the tag is found it replaces that instance, then converts the array back into a list and
     * returns that list.
     *
     * The reasoning for the List --> Array --> List conversions was due to errors being thrown and
     * not being able to replace items using list.set(e). Not sure why, but it is possible the
     * backstackDelegate uses Arrays.AsLists(t).
     */
    public static List<Object> replaceFirstInstanceOfGroupTag(List<Object> history, BaseKey newKey){

        Object[] historyArray = history.toArray();

        for (int i = 0; i < historyArray.length; i++) {

            String historyTag = ((BaseKey) historyArray[i]).getGroupTag();
            if (historyTag.equals(newKey.getGroupTag())) {

                historyArray[i] = newKey;

                //Now convert array back to a list
                List<Object> newHistory = new ArrayList<>();
                for (int j = 0; j < historyArray.length; j++) {
                    newHistory.add(historyArray[j]);
                }

                return newHistory;
            }

            System.out.println("Tag not found in history, returning old history");
        }

        return history;
    }
}
