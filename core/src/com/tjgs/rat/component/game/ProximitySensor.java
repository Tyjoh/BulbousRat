package com.tjgs.rat.component.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 */
public class ProximitySensor extends SerializableComponent<ProximitySensor> {

    public float radius;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() { return true; }

    @Override
    public void copyAndBuild(ProximitySensor component) {
        this.radius = component.radius;
    }

    @Override
    public void preSerialize() { }
}
