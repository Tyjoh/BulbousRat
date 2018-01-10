package com.tjgs.rat.event;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by Tyler Johnson on 4/25/2017.
 *
 * Encapsulates collision event data
 *
 */
public class CollisionEvent implements Event{

    public final Contact contact;
    public final int entityAId;
    public final int entityBId;
    public final boolean collisionStart;

    public CollisionEvent(Contact contact, int entityAId, int entityBId, boolean collisionStart) {
        this.contact = contact;
        this.entityAId = entityAId;
        this.entityBId = entityBId;
        this.collisionStart = collisionStart;
    }
}
