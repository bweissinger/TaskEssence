package com.madabysslabs.app.taskessence;

import java.util.ArrayList;

/**
 * Created by bryan on 8/8/17.
 */

public class HelpAndFeedbackDataSection {
    public final String sectionHeader;

    private final ArrayList<String> titles;
    private final ArrayList<String> bodies;

    public HelpAndFeedbackDataSection(String sectionHeader, ArrayList<String> titles, ArrayList<String> bodies){
        this.sectionHeader = sectionHeader;
        this.titles = titles;
        this.bodies = bodies;
    }

    public int sizeOfSection(){ return titles.size(); }

    public String getTitle(int position){
        if(position < 0){ return titles.get(0); }
        if(position >= titles.size()){ return titles.get(titles.size() - 1); }
        return titles.get(position);
    }

    public String getBody(int position){
        if(position < 0){ return bodies.get(0); }
        if(position >= bodies.size()){ return bodies.get(bodies.size() - 1); }
        return bodies.get(position);
    }

    public boolean shouldHaveDivider(int position){
        return position + 1 < titles.size();
    }

}
