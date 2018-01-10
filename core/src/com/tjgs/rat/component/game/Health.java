package com.tjgs.rat.component.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class Health extends SerializableComponent<Health> {

    public boolean drawHealth = true;
    public float health = 1;
    public float maxHealth = 1;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void copyAndBuild(Health component) {
        this.health = component.health;
        this.maxHealth = component.maxHealth;
        this.drawHealth = component.drawHealth;
    }

    @Override
    public void preSerialize() { }
}
