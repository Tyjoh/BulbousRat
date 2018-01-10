package com.tjgs.rat.event;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Tyler Johnson on 4/27/2017.
 *
 */
public class HealthDamageEvent implements Event{

    public final int attackerEntityId;
    public final float damageAmount;

    //used for knock back
    public final Vector2 damageDir;

    public HealthDamageEvent(Vector2 damageDir, float damageAmount, int attackerEntity){
        this.damageDir = damageDir;
        this.damageAmount = damageAmount;
        this.attackerEntityId = attackerEntity;
    }

}
