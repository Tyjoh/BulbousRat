package com.tjgs.rat.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.Array;
import com.tjgs.rat.input.controller.Controller;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class InputPollSystem extends BaseSystem{

    protected Array<Controller> controllers;

    public InputPollSystem(Array<Controller> controllers){
        this.controllers = controllers;
    }

    @Override
    protected void processSystem() {
        for(Controller controller: controllers){
            controller.pollIntents();
        }
    }
}
