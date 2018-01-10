package com.tjgs.rat.input.intent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class ActionControlIntent implements ControlIntent{

    public enum Action{
        LUNGE,
        GRAB,
    }

    private Action intent;
    private float scale;

    public ActionControlIntent(Action intent, float scale){
        this.intent = intent;
        this.scale = scale;
    }

    public Action getIntent() {
        return intent;
    }

    public float getScale() {
        return scale;
    }

}
