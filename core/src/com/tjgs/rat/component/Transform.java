package com.tjgs.rat.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/2/2017.
 *
 */

public class Transform extends SerializableComponent<Transform> implements Buildable<Transform> {

    public final Vector2 position = new Vector2();
    public float z = 0;
    public float angle = 0;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public void copyAndBuild(Transform transform) {
        this.position.set(transform.position);
        this.z = transform.z;
        this.angle = transform.angle;
    }

    @Override
    public void preSerialize() { }

    @Override
    public boolean isValid() {
        return true;
    }

}
