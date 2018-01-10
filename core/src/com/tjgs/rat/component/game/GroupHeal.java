package com.tjgs.rat.component.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 */
public class GroupHeal extends SerializableComponent<GroupHeal> {

    public float healRate = 1;
    public int numToHeal = 1;
    public int healGroupId = -1;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() { return false; }

    @Override
    public void copyAndBuild(GroupHeal component) {
        this.healRate = component.healRate;
        this.numToHeal = component.numToHeal;
        this.healGroupId = component.healGroupId;
    }

    @Override
    public void preSerialize() { }
}
