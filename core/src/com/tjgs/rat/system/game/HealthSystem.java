package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.game.Health;
import com.tjgs.rat.event.DeathEvent;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.event.HealthDamageEvent;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class HealthSystem extends IteratingSystem {

    private ComponentMapper<Health> mHealth;

    @Wire
    private EventBlackboard eventBlackboard;

    public HealthSystem(){
        super(Aspect.all(Health.class));
    }

    @Override
    protected void begin() {
        eventBlackboard.clearEventType(DeathEvent.class);
    }

    @Override
    protected void process(int entityId) {
        Health health = mHealth.get(entityId);

        if(!eventBlackboard.entityHasEvent(HealthDamageEvent.class, entityId)){
            return;
        }

        Queue<HealthDamageEvent> damageEvents = eventBlackboard.queryEntityEvents(HealthDamageEvent.class,  entityId);

        for(HealthDamageEvent damageEvent: damageEvents){
            doDamage(damageEvent, entityId);
            if (health.health <= 0){
                //removeEntity
                System.out.println("Entity " + entityId + " died");
                DeathEvent event = new DeathEvent(damageEvent.attackerEntityId);
                eventBlackboard.addEvent(event, entityId);

                world.delete(entityId);
                break;
            }
        }

    }

    private void doDamage(HealthDamageEvent damageEvent, int entity){

        if (mHealth.has(entity)) {
            Health health = mHealth.get(entity);
            health.health -= damageEvent.damageAmount;
        }

    }

    @Override
    protected void end() {
    }
}
