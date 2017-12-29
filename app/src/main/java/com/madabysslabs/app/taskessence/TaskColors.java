package com.madabysslabs.app.taskessence;

import java.util.Random;

public class TaskColors {

    private final int[] availableColors = {
            R.color.taskColor1,
            R.color.taskColor2,
            R.color.taskColor3,
            R.color.taskColor4,
            R.color.taskColor5
    };

    public int[] getTaskColors(int numberOfTasks){
        int[] taskColors = new int[numberOfTasks];
        Random rand = new Random();

        for(int i = 0; i < taskColors.length; i++){
            if(taskColors.length > availableColors.length){
                taskColors[i] = availableColors[rand.nextInt(availableColors.length - 1)];
            }
            else{
                while(true){
                    int tempColor = availableColors[rand.nextInt(availableColors.length - 1)];
                    Boolean colorAlreadyUsed = false;
                    for(int j = 0; j < taskColors.length; j++){
                        if(taskColors[j] == tempColor){
                            colorAlreadyUsed = true;
                        }
                    }
                    if(!colorAlreadyUsed){
                        taskColors[i] = tempColor;
                        break;
                    }
                }
            }
        }
        return taskColors;
    }

}
