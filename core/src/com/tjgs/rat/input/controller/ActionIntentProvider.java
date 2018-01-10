package com.tjgs.rat.input.controller;

import com.tjgs.rat.input.intent.ActionControlIntent;
import com.tjgs.rat.input.intent.IntentRegistry;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public interface ActionIntentProvider {
    IntentRegistry<ActionControlIntent> getActionIntentRegistry();
}
