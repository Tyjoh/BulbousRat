package com.tjgs.rat.event;

/**
 * Created by Tyler Johnson on 4/27/2017.
 *
 */
public class DeathEvent implements Event{

    public final int killerEntityId;

    public DeathEvent(int killerEntityId) {
        this.killerEntityId = killerEntityId;
    }
}
