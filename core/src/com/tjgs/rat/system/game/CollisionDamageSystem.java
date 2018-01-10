package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.game.Damager;
import com.tjgs.rat.component.game.Health;
import com.tjgs.rat.event.CollisionEvent;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.event.HealthDamageEvent;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class CollisionDamageSystem extends IteratingSystem {

    private ComponentMapper<Health> mHealth;
    private ComponentMapper<Damager> mDamager;

    @Wire
    private EventBlackboard eventBlackboard;

    public CollisionDamageSystem(){
        super(Aspect.all(Damager.class));
    }

    @Override
    protected void begin() {
        eventBlackboard.clearEventType(HealthDamageEvent.class);
    }

    @Override
    protected void process(int entityId) {

        if(!eventBlackboard.entityHasEvent(CollisionEvent.class, entityId)) {
            return;
        }

        Queue<CollisionEvent> collisionEvents = eventBlackboard.queryEntityEvents(CollisionEvent.class, entityId);

        //iterate over all collision events
        for(CollisionEvent collisionEvent: collisionEvents) {
            testCollision(collisionEvent, entityId);
        }
    }

    public void testCollision(CollisionEvent collisionEvent, int entityId){
        //this check is required due to collisions only being reported once for the two bodies
        //the same event is added to both entityA and entityB's event queue
        if(entityId == collisionEvent.entityAId && mHealth.has(collisionEvent.entityBId)) {
            //if this entity is entityA, do damage to entityB
            Damager damager = mDamager.get(entityId);

            //test cool down
            if (damager.canUseNow()) {
                Vector2 damageDir = new Vector2();

                //create the damage event
                HealthDamageEvent event = new HealthDamageEvent(damageDir, damager.damage, entityId);
                eventBlackboard.addEvent(event, collisionEvent.entityBId);

                //activate cool down
                damager.setDamagerUsed();
            }

        }else if(entityId == collisionEvent.entityBId && mHealth.has(collisionEvent.entityBId)){
            //if this entity is entityB, do damage to entityA
            Damager damager = mDamager.get(entityId);

            //test cool down
            if (damager.canUseNow()) {
                Vector2 damageDir = new Vector2();

                //create the damage event
                HealthDamageEvent event = new HealthDamageEvent(damageDir, damager.damage, collisionEvent.entityBId);
                eventBlackboard.addEvent(event, entityId);

                //activate cool down
                damager.setDamagerUsed();
            }
        }
    }

}
