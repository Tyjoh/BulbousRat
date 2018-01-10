package com.tjgs.rat.input.intent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public interface IntentTracker<I> {

    void intentCreated(I intent, int controllerId);

}
