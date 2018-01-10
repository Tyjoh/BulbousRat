package com.tjgs.rat.input.intent;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class IntentRegistry<T extends ControlIntent> {

    private Array<IntentTracker<T>> intentTrackers;

    public IntentRegistry(){
        intentTrackers = new Array<IntentTracker<T>>();
    }

    public void registerIntentTracker(IntentTracker<T> tracker){
        intentTrackers.add(tracker);
    }

    public void removeIntentTracker(IntentTracker<T> tracker){
        intentTrackers.removeValue(tracker, true);
    }

    public void notifyTrackers(T t, int controllerId){
        for(IntentTracker<T> tracker: intentTrackers){
            tracker.intentCreated(t, controllerId);
        }
    }

}
