package com.tjgs.rat.event;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 */
public class ProximityEvent implements Event{

    public final float distance;
    public final int sensorEntityId;
    public final int testEntityId;

    public ProximityEvent(float distance, int sensorEntityId, int testEntityId) {
        this.distance = distance;
        this.sensorEntityId = sensorEntityId;
        this.testEntityId = testEntityId;
    }
}
