package com.madabysslabs.app.taskessence;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by bryan on 8/8/17.
 */

public class HelpAndFeedbackRecyclerSection extends StatelessSection {

    private RecyclerViewClickListener listener;
    private final HelpAndFeedbackDataSection section;

    public HelpAndFeedbackRecyclerSection(HelpAndFeedbackDataSection section, RecyclerViewClickListener listener) {
        // call constructor with layout resources for this Section header and items
        super(new SectionParameters.Builder(R.layout.help_list_item)
                .headerResourceId(R.layout.help_list_section_header)
                .build());

        this.listener = listener;
        this.section = section;
    }

    @Override
    public int getContentItemsTotal() {
        return section.sizeOfSection();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new RecyclerViewHolder(view, listener);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new RecyclerViewHeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        RecyclerViewHeaderHolder headerHolder = (RecyclerViewHeaderHolder) holder;

        headerHolder.header.setText(section.sectionHeader);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        recyclerViewHolder.title.setText(section.getTitle(position));
        recyclerViewHolder.text.setText(section.getBody(position));

        if(!section.shouldHaveDivider(position)){
            recyclerViewHolder.RemoveDivider();
        }

    }

}
