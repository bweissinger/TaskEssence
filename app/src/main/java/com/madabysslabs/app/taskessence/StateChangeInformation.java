package com.madabysslabs.app.taskessence;

import com.zhuinden.simplestack.StateChange;

/**
 * Created by bryan on 7/23/17.
 */

/* Wraps the current state change in a
 *
 */
public class StateChangeInformation {
    private StateChange stateChange;

    public StateChangeInformation(){}

    public int getNewBackstackSize(){
        if(stateChange == null){
            throw new IllegalArgumentException("stateChange is null!");
        }
        return stateChange.getNewState().size();
    }

    public boolean stateChangeIsActive(){ return stateChange != null; }

    public void setStateChangeComplete(){ stateChange = null; }

    public void setNewStateChangeInProgress(StateChange stateChange){
        this.stateChange = stateChange;
    }
}
