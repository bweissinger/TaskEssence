package com.madabysslabs.app.taskessence;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpAndFeedback extends BaseFragment {

    private static int[] sectionMap;

    public HelpAndFeedback() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_help_and_feedback, container, false);

        //Enable back button for this fragment
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if(((MainActivity) getActivity()).canGoBack()){
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            }
        }

        /*
        * Counter-intuitive, but must set true in order for onCreateOptionsMenu to be called
        * where the menu can then be removed by menu.clear
        * setHasOptionsMenu(false) will not remove the menu
        */
        setHasOptionsMenu(true);

        prepareHelp(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void prepareHelp(View view) {
        //Prepare and get sections
        HelpAndFeedbackDataPreparer helpAndFeedbackDataPreparer = new HelpAndFeedbackDataPreparer(
                getResources().getStringArray(R.array.help_item_titles),
                getResources().getStringArray(R.array.help_item_answers),
                getResources().getStringArray(R.array.help_item_sections));
        ArrayList<HelpAndFeedbackDataSection> helpAndFeedbackDataSections = helpAndFeedbackDataPreparer.getSections();

        //Setup on click listener
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppPreferences appPreferences = new AppPreferences(getContext());
                appPreferences.setHelpMenuCursorPosition(sectionMap[position]);
                MainActivity.get(getContext()).navigateTo(HelpExpandedKey.create());
            }
        };

        //Prepare sectioned recycler view adapter and add sections
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        for(int i = 0; i < helpAndFeedbackDataSections.size(); i++){
            sectionAdapter.addSection(new HelpAndFeedbackRecyclerSection(helpAndFeedbackDataSections.get(i), listener));
        }

        /*
         * The cursor position takes into account the sections as well as the items. So Section 0
         * item 1 registers as position 1 instead of position 0. This creates a map array that maps
         * the cursor position to the correct position in the array.
         */
        sectionMap = new int[sectionAdapter.getItemCount()];
        for(int i = 0, sections = 1, prevSection = 0; i < sectionMap.length; i++) {
            if(prevSection != sectionAdapter.getSectionPosition(sectionAdapter.getSectionForPosition(i)))
            {
                sections++;
                prevSection = sectionAdapter.getSectionPosition(sectionAdapter.getSectionForPosition(i));
            }
            sectionMap[i] = i - sections;
        }

        //Setup recycler view with sectioned recycler view adapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.help_and_feedback_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Remove the menu from this fragment
        menu.clear();
    }
}
