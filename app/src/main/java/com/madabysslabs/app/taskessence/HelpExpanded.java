package com.madabysslabs.app.taskessence;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by bryan on 8/13/17.
 */

public class HelpExpanded extends BaseFragment {

    public HelpExpanded() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_help_expanded, container, false);

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
        //Get layouts
        TypedArray layoutArray = getContext().getResources().obtainTypedArray(R.array.help_item_layouts);

        //Get cursor position and set up text views
        AppPreferences appPreferences = new AppPreferences(getContext());

        //Get selected help item layout
        int layoutId = layoutArray.getResourceId(appPreferences.getHelpMenuCursorPosition(), 0);

        //Add layout to the card view
        CardView card = (CardView) view.findViewById(R.id.help_expanded_card);
        card.addView(LayoutInflater.from(getContext()).inflate(layoutId, (ViewGroup)view.getParent(), false));

        //Recycle typed array
        layoutArray.recycle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Remove the menu from this fragment
        menu.clear();
    }
}
