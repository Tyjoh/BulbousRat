package com.tjgs.rat.util.physics;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Tyler Johnson on 4/8/2017.
 *
 */

public class PhysicsWorld {

    public final World world;

    public PhysicsWorld(Vector2 gravity, boolean doSleep){
        world = new World(gravity, doSleep);
    }
}
