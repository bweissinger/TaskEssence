package com.madabysslabs.app.taskessence;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by bryan on 7/27/17.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected ImageView icon;
    protected TextView title;
    protected TextView text;
    protected View separator;
    protected ViewGroup layout;
    private RecyclerViewClickListener listener;

    public RecyclerViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
        icon = (ImageView) itemView.findViewById(R.id.help_item_icon);
        title = (TextView) itemView.findViewById(R.id.help_item_title);
        text = (TextView) itemView.findViewById(R.id.help_item_text);
        separator = (View) itemView.findViewById(R.id.help_item_separator);
        layout = (LinearLayout) itemView.findViewById(R.id.help_item_layout);
    }

    public void RemoveDivider(){
        layout.removeView(separator);
    }

    @Override
    public void onClick(View view){
        listener.onClick(view, getAdapterPosition());
    }
}
