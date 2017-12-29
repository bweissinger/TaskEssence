package com.madabysslabs.app.taskessence;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import java.util.Locale;

/**
 * Created by bryan on 7/13/17.
 */

public class UtilityClass {

    static public void showErrorAlertDialog(Context context, String title, String error){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static int parseHour(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[0]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int parseMinute(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[1]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static String timeToString(int h, int m)
    {
        return String.format(Locale.US, "%02d", h) + ":" + String.format(Locale.US, "%02d", m);
    }

    public static String convertWholeNumberToPlace(int wholeNumber, boolean capitalize){

        String place;

        switch (wholeNumber){
            case 1:
                place = "first";
                break;

            case 2:
                place = "second";
                break;

            case 3:
                place = "third";
                break;

            case 4:
                place = "fourth";
                break;

            case 5:
                place = "fifth";
                break;

            case 6:
                place = "sixth";
                break;

            case 7:
                place = "seventh";
                break;

            case 8:
                place = "eighth";
                break;

            case 9:
                place = "ninth";
                break;

            default:
                place = "out of range";
                break;
        }

        if(capitalize){
            place = capitalizeFirstLetterOfWord(place);
        }

        return place;

    }

    public static String capitalizeFirstLetterOfWord(String wordToCapitalize){

        wordToCapitalize = wordToCapitalize.toLowerCase();
        return Character.toUpperCase(wordToCapitalize.charAt(0)) + wordToCapitalize.substring(1);

    }

}
