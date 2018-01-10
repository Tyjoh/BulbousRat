package com.tjgs.rat.component.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class ActionController extends SerializableComponent<ActionController> {

    public int controllerId = 0;

    public float lungeForwardSpeed = 1;
    public float lungeBackwardSpeed = 1;
    public long lungeCoolDown = 100;
    public transient long lastLunge = 0;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() { return true; }

    @Override
    public void copyAndBuild(ActionController component) {
        this.lungeForwardSpeed = component.lungeForwardSpeed;
        this.lungeBackwardSpeed = component.lungeBackwardSpeed;
        this.lungeCoolDown = component.lungeCoolDown;
        this.controllerId = component.controllerId;
    }

    @Override
    public void preSerialize() { }
}
