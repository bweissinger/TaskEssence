package com.madabysslabs.app.taskessence;

import java.util.ArrayList;

/**
 * Created by bryan on 8/4/17.
 */

public class HelpAndFeedbackDataPreparer {

    final private String[] titles;
    final private String[] bodies;
    final private String[] sections;

    public HelpAndFeedbackDataPreparer(String[] titles, String[] bodies, String[] sections){
        this.titles = titles;
        this.bodies = bodies;
        this.sections = sections;
    }

    public ArrayList<HelpAndFeedbackDataSection> getSections(){

        ArrayList<HelpAndFeedbackDataSection> dataSections = new ArrayList<>();

        int startPosition = 0;
        int endPosition = 0;
        while(endPosition < sections.length - 1){

            endPosition = getSectionEndPosition(startPosition);

            ArrayList<String> sectionTitles = new ArrayList<>();
            ArrayList<String> sectionBodies = new ArrayList<>();
            for(int i = startPosition; i <= endPosition; i++){
                sectionTitles.add(titles[i]);
                sectionBodies.add(bodies[i]);
            }
            dataSections.add(new HelpAndFeedbackDataSection(
                    sections[startPosition],
                    sectionTitles,
                    sectionBodies
            ));

            startPosition = endPosition + 1;

        }

        return dataSections;

    }

    private int getSectionEndPosition(int sectionStartPosition){

        for(int i = sectionStartPosition; i < sections.length - 1; i++){
            if(!sections[i].equals(sections[i + 1])) { return i; }
        }
        return sections.length - 1;

    }

}
