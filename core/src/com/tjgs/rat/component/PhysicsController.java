package com.tjgs.rat.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.input.intent.IntentTracker;
import com.tjgs.rat.input.intent.PhysicsControlIntent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class PhysicsController extends SerializableComponent<PhysicsController> {

    public float maxLinearSpeed = 2.1f;
    public float linearAccel = 0.2f;

    public float maxAngularSpeed = 1.8f;
    public float angularAccel = 0.15f;

    //0 always represents keyboard, 0 can also mean controller id 0.
    public int controllerId = 0;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public void copyAndBuild(PhysicsController controller) {
        this.maxLinearSpeed = controller.maxLinearSpeed;
        this.linearAccel = controller.linearAccel;
        this.maxAngularSpeed = controller.maxAngularSpeed;
        this.angularAccel = controller.angularAccel;
        this.controllerId = controller.controllerId;
    }

    @Override
    public void preSerialize() { }

    @Override
    public boolean isValid() {
        return true;
    }
}
