package com.madabysslabs.app.taskessence;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskCard {

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private static final float CARD_RAISED_HEIGHT = 8.0f;
    private static final float CARD_REGULAR_HEIGHT = 2.0f;

    private CurrentTasksView mCurrentTasksView;
    private RelativeLayout taskCardsBackground;
    private CardView mCard;
    private TextView mTaskNumberTextView;
    private TextView mTaskTextView;
    private RelativeLayout mTaskNumberMarker;
    private ImageButton okButton;
    private ImageButton unselectButton;
    private int mCardColor;
    private int selectedColor;
    private int primaryColor;
    private int mTaskNumber;
    private int mTaskTextCompletedColor;
    private String mTaskText;
    private boolean mIsCompleted;

    public TaskCard(CurrentTasksView currentTasksView,
                    View rootView,
                    int taskNumber,
                    int cardColor,
                    String taskText,
                    boolean isCompleted,
                    Resources.Theme theme){

        //Get context of root view
        Context context = rootView.getContext();

        //Find the package name for the application
        String packageName = context.getPackageName();

        //Find the card view and sub views
        taskCardsBackground = (RelativeLayout) rootView.findViewById(R.id.task_cards_background);

        mCard = (CardView) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                                "task_card_" + taskNumber,
                                "id",
                                packageName)
        );

        mTaskNumberTextView = (TextView) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                                "task_card_" + taskNumber + "_marker_number",
                                "id",
                                packageName)
        );

        mTaskNumberMarker = (RelativeLayout) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                                "task_card_" + taskNumber + "_marker",
                                "id",
                                packageName));

        mTaskTextView = (TextView) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                            "task_card_" + taskNumber + "_text",
                            "id",
                            packageName)
        );

        okButton = (ImageButton) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                                "ok_button_" + taskNumber,
                                "id",
                                packageName)
        );

        unselectButton = (ImageButton) rootView.findViewById(
                context.getResources()
                        .getIdentifier(
                                "unselect_button_" + taskNumber,
                                "id",
                                packageName)
        );


        //Assign to class variables
        mCurrentTasksView = currentTasksView;
        mCardColor = cardColor;
        mTaskText = taskText;
        mIsCompleted = isCompleted;
        mTaskNumber = taskNumber;

        //get the theme's task finished card color
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorCardViewNotSelectable, typedValue, true);
        primaryColor = typedValue.data;

        //get card view selected color from current theme
        theme.resolveAttribute(R.attr.colorCardViewSelected, typedValue, true);
        selectedColor = typedValue.data;

        //get card view text color for completed items
        theme.resolveAttribute(R.attr.textColorSecondaryCardView, typedValue, true);
        mTaskTextCompletedColor = typedValue.data;

        //Setup each card
        initCardItems();
    }


    //When a card is clicked, it gains focus, then onfocuschange handles the logic
    //When the scroll view is clicked, it gains focus and no cards are focused
    private void requestFocusOnClickListener(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });
    }

    private void setCardOnFocusChangeListener() {
        mCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!mIsCompleted){
                    if (hasFocus) {
                        cardFocused();
                    }
                    else {
                        cardNotFocused();
                    }
                }
            }
        });
    }

    private void setOkButtonOnClickListener(){
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            taskCompleted();
            }
        });
    }

    private void setUnselectButtonOnClickListener(){
        unselectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCard.clearFocus();
            }
        });
    }

    private void initCardItems() {
        mCard.setCardBackgroundColor(mCardColor);
        mTaskNumberTextView.setTextColor(mCardColor);
        mTaskTextView.setText(mTaskText, TextView.BufferType.SPANNABLE);

        //Set unselect button to color of card
        unselectButton.setColorFilter(mCardColor);

        //Set max elevation and starting elevation
        mCard.setMaxCardElevation(CARD_RAISED_HEIGHT);
        mCard.setCardElevation(CARD_REGULAR_HEIGHT);

        //Setup conditions for when user clicks on items
        requestFocusOnClickListener(mCard);
        requestFocusOnClickListener(taskCardsBackground);
        setCardOnFocusChangeListener();
        setOkButtonOnClickListener();
        setUnselectButtonOnClickListener();

        //Now reset focus
        cardNotFocused();

        //If task is not entered/already completed, then show as completed
        if(mIsCompleted){
            taskCompleted();
        }
    }

    private void cardNotFocused(){
        //Reset card to regular status
        mCard.setCardElevation(CARD_REGULAR_HEIGHT);
        mTaskNumberTextView.setVisibility(View.VISIBLE);
        mTaskTextView.setVisibility(View.VISIBLE);
        mTaskNumberMarker.setVisibility(View.VISIBLE);
        mCard.setCardBackgroundColor(mCardColor);

        //Hide buttons
        okButton.setVisibility(View.INVISIBLE);
        unselectButton.setVisibility(View.INVISIBLE);
    }

    private void cardFocused(){
        if(!mIsCompleted){
            //Raise card and change colors
            mCard.setCardElevation(CARD_RAISED_HEIGHT);
            mTaskNumberTextView.setVisibility(View.INVISIBLE);
            mTaskTextView.setVisibility(View.INVISIBLE);
            mTaskNumberMarker.setVisibility(View.INVISIBLE);
            mCard.setCardBackgroundColor(selectedColor);

            //Make buttons visible
            okButton.setVisibility(View.VISIBLE);
            unselectButton.setVisibility(View.VISIBLE);
        }
    }

    private void taskCompleted(){
        //Strike through text
        Spannable spannable = (Spannable) mTaskTextView.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN,
                0,
                mTaskText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Set task as completed
        mIsCompleted = true;

        //Change color card to match background and reset height
        mCard.setCardBackgroundColor(primaryColor);
        mCard.setCardElevation(CARD_REGULAR_HEIGHT);

        //Set text to visible
        mTaskTextView.setVisibility(View.VISIBLE);
        mTaskTextView.setTextColor(mTaskTextCompletedColor);

        //Hide task marker and number
        mTaskNumberMarker.setVisibility(View.INVISIBLE);
        mTaskNumberTextView.setVisibility(View.INVISIBLE);

        //Hide card buttons
        okButton.setVisibility(View.INVISIBLE);
        unselectButton.setVisibility(View.INVISIBLE);

        //Now unfocus from card
        mCard.clearFocus();

        //Call fragment and let it know that the task was completed
        mCurrentTasksView.taskCompleted(mTaskNumber);
    }

}
