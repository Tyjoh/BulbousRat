package com.tjgs.rat.component.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class Damager extends SerializableComponent<Damager> {

    public int damage;
    public long coolDown = 1500;
    public transient long lastDamageTime;

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() { return true; }

    @Override
    public void copyAndBuild(Damager component) {
        damage = component.damage;
        coolDown = component.coolDown;
        lastDamageTime = 0;
    }

    @Override
    public void preSerialize() { }

    /**
     * Helper method to make system code slightly cleaner
     * @return true if damager can be used
     */
    public boolean canUseNow(){
        return System.currentTimeMillis() - lastDamageTime >= coolDown;
    }

    public void setDamagerUsed(){
        lastDamageTime = System.currentTimeMillis();
    }
}
