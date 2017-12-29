package com.madabysslabs.app.taskessence;

/**
 * Created by bryan on 7/21/17.
 */

public class AnimationContainer {
    public final int enterAnimationForward;
    public final int exitAnimationForward;
    public final int enterAnimationBackward;
    public final int exitAnimationBackward;

    public AnimationContainer(int enterAnimationForward,
                              int enterAnimationBackward,
                              int exitAnimationForward,
                              int exitAnimationBackward){
        this.enterAnimationForward = enterAnimationForward;
        this.enterAnimationBackward = enterAnimationBackward;
        this.exitAnimationForward = exitAnimationForward;
        this.exitAnimationBackward = exitAnimationBackward;
    }
}
