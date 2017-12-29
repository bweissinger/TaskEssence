package com.madabysslabs.app.taskessence;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by bryan on 8/8/17.
 */

public class RecyclerViewHeaderHolder extends RecyclerView.ViewHolder {

    protected TextView header;

    public RecyclerViewHeaderHolder(View itemView) {
        super(itemView);
        header = (TextView) itemView.findViewById(R.id.help_and_feedback_section_header);
    }
}
