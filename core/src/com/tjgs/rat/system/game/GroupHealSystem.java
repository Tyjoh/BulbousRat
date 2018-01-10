package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.game.GroupHeal;
import com.tjgs.rat.component.game.Health;
import com.tjgs.rat.component.game.ProximitySensor;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.event.ProximityEvent;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 */
public class GroupHealSystem extends IteratingSystem{

    private ComponentMapper<Health> mHealth;
    private ComponentMapper<GroupHeal> mHeal;
    private ComponentMapper<ProximitySensor> mProximity;

    @Wire
    private EventBlackboard eventBlackboard;

    public GroupHealSystem(){
        super(Aspect.all(GroupHeal.class, Health.class, ProximitySensor.class));
    }

    @Override
    protected void process(int entityId) {

        GroupHeal groupHeal = mHeal.get(entityId);

        if(!eventBlackboard.entityHasEvent(ProximityEvent.class, entityId)){
            return;
        }

        Queue<ProximityEvent> proximityEvents = eventBlackboard.queryEntityEvents(ProximityEvent.class, entityId);

        //if there is enough proximity events to cause healing
        if(proximityEvents.size >= groupHeal.numToHeal){
            //System.out.println("testing for group healing");
            int numHealersFound = 0;

            //search for number of appropriate healers
            for(ProximityEvent event: proximityEvents){
                if(!mHeal.has(event.testEntityId)) continue;//if test entity has heal component
                GroupHeal testGroupHeal = mHeal.get(event.testEntityId);

                if(testGroupHeal.healGroupId == groupHeal.healGroupId){
                    numHealersFound ++;
                    //if enough found quit searching
                    if(numHealersFound >= groupHeal.numToHeal) break;
                }
            }

            //if required amount of healers is found
            if(numHealersFound >= groupHeal.numToHeal){

                Health entityHealth = mHealth.get(entityId);

                entityHealth.health += groupHeal.healRate * world.getDelta();//add health per second

                //clamp health
                if(entityHealth.health > entityHealth.maxHealth){
                    entityHealth.health = entityHealth.maxHealth;
                }

            }

        }

    }
}
