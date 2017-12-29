package com.madabysslabs.app.taskessence;

/**
 * Created by bryan on 7/12/17.
 */

public class HelpItem{

    private final String title;
    private final String text;
    private final String helpType;
    public static final String TITLE_ID = "Title";
    public static final String TEXT_ID = "Text";
    public static final String ARTICLE = "Article";
    public static final String FAQ = "Faq";

    public HelpItem(String title, String text, String helpType){
        this.title = title;
        this.text = text;
        this.helpType = verifyHelpType(helpType);
    }

    //If type is not any of the available types it defaults to article
    private String verifyHelpType(String selectedHelpType){

        if(!selectedHelpType.equals(ARTICLE) &&
                !selectedHelpType.equals(FAQ)){
            return ARTICLE;
        }

        return selectedHelpType;
    }

    public String getTitle(){
        return title;
    }

    public String getText(){
        return text;
    }

    public String getHelpType() { return helpType; }
}
