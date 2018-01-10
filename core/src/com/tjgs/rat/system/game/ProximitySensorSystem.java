package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.component.game.ProximitySensor;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.event.ProximityEvent;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 */
public class ProximitySensorSystem extends IteratingSystem {

    private ComponentMapper<ProximitySensor> mProximity;
    private ComponentMapper<Transform> mTransform;

    @Wire
    private EventBlackboard eventBlackboard;

    private EntitySubscription transformsSubs;

    public ProximitySensorSystem(){
        super(Aspect.all(ProximitySensor.class, Transform.class));
    }

    @Override
    protected void initialize() {
        transformsSubs = world.getAspectSubscriptionManager().get(Aspect.all(Transform.class));
    }

    @Override
    public void begin(){
        //clear out old events
        eventBlackboard.clearEventType(ProximityEvent.class);
    }

    @Override
    protected void process(int entityId) {

        IntBag entities = transformsSubs.getEntities();

        for(int i = 0; i < entities.size(); i++){
            int testEntity = entities.get(i);
            if(testEntity != entityId) {
                testEntityProximity(entityId, testEntity);
            }
        }

    }

    private void testEntityProximity(int sensorEntity, int testEntity){
        ProximitySensor sensor = mProximity.get(sensorEntity);

        Vector2 sensorPos = mTransform.get(sensorEntity).position;
        Vector2 testPos = mTransform.get(testEntity).position;

        float radSquare = sensor.radius * sensor.radius;
        float distSquare = sensorPos.dst2(testPos);

        if(distSquare <= radSquare){
            //test entity is within proximity
            float dist = ((float) Math.sqrt(distSquare));
            ProximityEvent event = new ProximityEvent(dist, sensorEntity, testEntity);

            eventBlackboard.addEvent(event, sensorEntity);
        }

    }
}
