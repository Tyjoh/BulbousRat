package com.tjgs.rat.input.controller;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public abstract class Controller {

    protected int controllerId;

    public Controller(int controllerId){
        this.controllerId = controllerId;
    }

    public abstract void pollIntents();

}
