package com.tjgs.rat.input.intent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class PhysicsControlIntent implements ControlIntent {

    public enum Action{
        FORWARD(),
        BACKWARD(),
        LEFT(),
        RIGHT(),
        ROTATE_RIGHT(),
        ROTATE_LEFT()
    }

    private Action intent;
    private float scale;

    public PhysicsControlIntent(Action intent, float scale){
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
