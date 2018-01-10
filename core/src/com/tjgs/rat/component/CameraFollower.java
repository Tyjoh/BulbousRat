package com.tjgs.rat.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/27/2017.
 *
 */
public class CameraFollower extends SerializableComponent<CameraFollower> {

    public float tweenSpeed = 0.055f;
    public float tweenCutoff = 0.00125f;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() { return true; }

    @Override
    public void copyAndBuild(CameraFollower component) {
        this.tweenSpeed = component.tweenSpeed;
        this.tweenCutoff = component.tweenCutoff;
    }

    @Override
    public void preSerialize() { }
}
