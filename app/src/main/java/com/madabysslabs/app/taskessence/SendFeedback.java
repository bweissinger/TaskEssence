package com.madabysslabs.app.taskessence;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SendFeedback extends DialogFragment{

    private String[] subjects;
    private String[] bodyTexts;
    private int mSelectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        subjects = getResources().getStringArray(R.array.email_subjects);
        bodyTexts = getResources().getStringArray(R.array.email_body_texts);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_feedback)
                .setSingleChoiceItems(R.array.feedback_types, 0,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedItem = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendEmail(getActivity().getApplicationContext(), subjects[mSelectedItem], bodyTexts[mSelectedItem]);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public static void sendEmail(Context context, String subject, String text){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setData(Uri.parse("mailto:" + "madabysslabs@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            context.startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
