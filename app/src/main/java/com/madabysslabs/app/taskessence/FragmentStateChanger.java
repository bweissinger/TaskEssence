package com.madabysslabs.app.taskessence;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhuinden.simplestack.StateChange;

/**
 * Created by bryan on 7/19/17.
 */

class FragmentStateChanger {
    private FragmentManager fragmentManager;
    private int containerId;

    public FragmentStateChanger(FragmentManager fragmentManager, int containerId){
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    private FragmentTransaction setAnimations(FragmentTransaction fragmentTransaction, StateChange stateChange){

        AnimationContainer newStateAnimations = null;
        AnimationContainer oldStateAnimations = null;

        newStateAnimations = ((BaseKey) stateChange
                .getNewState()
                .get(stateChange
                        .getNewState()
                        .size() - 1))
                .getAnimations();

        //Previous state could possibly be empty, check to make sure a previous state exists
        if(stateChange.getPreviousState().size() > 0){
            oldStateAnimations = ((BaseKey) stateChange
                    .getPreviousState()
                    .get(stateChange
                            .getPreviousState()
                            .size() - 1))
                    .getAnimations();
        }

        int enterAnimation = 0;
        int exitAnimation = 0;

        if(stateChange.getDirection() == StateChange.FORWARD){
            enterAnimation = newStateAnimations.enterAnimationForward;
            if(oldStateAnimations != null){
                exitAnimation = oldStateAnimations.exitAnimationForward;
            }
            else{
                //Defaults to new state exit animation if old state is null
                exitAnimation = newStateAnimations.exitAnimationForward;
            }
        }
        else if(stateChange.getDirection() == StateChange.BACKWARD) {
            exitAnimation = newStateAnimations.exitAnimationBackward;
            if(oldStateAnimations != null){
                enterAnimation = oldStateAnimations.enterAnimationBackward;
            }
            else{
                //Defaults to new state exit animation if old state is null
                enterAnimation = newStateAnimations.enterAnimationBackward;
            }
        }

        return fragmentTransaction.setCustomAnimations(enterAnimation, exitAnimation, enterAnimation, exitAnimation);
    }

    public void handleStateChange(StateChange stateChange){

        FragmentTransaction fragmentTransaction =
                setAnimations((fragmentManager
                                .beginTransaction()
                                .disallowAddToBackStack()),
                        stateChange);

        for(Object _oldKey : stateChange.getPreviousState()){
            BaseKey oldKey = (BaseKey) _oldKey;
            Fragment fragment = fragmentManager.findFragmentByTag(oldKey.getFragmentTag());
            if(fragment != null){
                if(!stateChange.getNewState().contains(oldKey)){
                    fragmentTransaction.remove(fragment);
                }
                else if(!fragment.isDetached()){
                    fragmentTransaction.detach(fragment);
                }
            }
        }
        for(Object _newKey: stateChange.getNewState()){
            BaseKey newKey = (BaseKey) _newKey;
            Fragment fragment = fragmentManager.findFragmentByTag(newKey.getFragmentTag());
            if(newKey.equals(stateChange.topNewState())){
                if(fragment != null){
                    if(fragment.isDetached()){
                        fragmentTransaction.attach(fragment);
                    }
                }
                else {
                    fragment = newKey.newFragment();
                    fragmentTransaction.add(containerId, fragment, newKey.getFragmentTag());
                }
            }
            else {
                if(fragment != null && !fragment.isDetached()){
                    fragmentTransaction.detach(fragment);
                }
            }
        }
        fragmentTransaction.commitNow();
    }
}
