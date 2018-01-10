package com.tjgs.rat.input.controller;

import com.tjgs.rat.input.intent.IntentRegistry;
import com.tjgs.rat.input.intent.PhysicsControlIntent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public interface PhysicsIntentProvider {
    IntentRegistry<PhysicsControlIntent> getPhysicsIntentRegistry();
}
