package com.tjgs.rat.input.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.input.intent.ActionControlIntent;
import com.tjgs.rat.input.intent.IntentRegistry;
import com.tjgs.rat.input.intent.PhysicsControlIntent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class KeyboardController extends Controller implements PhysicsIntentProvider, ActionIntentProvider{

    private final IntentRegistry<PhysicsControlIntent> physicsIntentTrackers;
    private final IntentRegistry<ActionControlIntent> actionIntentTrackers;

    private ObjectMap<PhysicsControlIntent.Action, Integer> physicsControlMap;

    private ObjectMap<ActionControlIntent.Action, Integer> actionControlMap;

    public KeyboardController(int controllerId) {
        super(controllerId);
        physicsIntentTrackers = new IntentRegistry<PhysicsControlIntent>();
        actionIntentTrackers = new IntentRegistry<ActionControlIntent>();

        physicsControlMap = new ObjectMap<PhysicsControlIntent.Action, Integer>();

        //define physics controller key bindings
        physicsControlMap.put(PhysicsControlIntent.Action.FORWARD, Input.Keys.W);
        physicsControlMap.put(PhysicsControlIntent.Action.BACKWARD, Input.Keys.S);
        physicsControlMap.put(PhysicsControlIntent.Action.LEFT, Input.Keys.A);
        physicsControlMap.put(PhysicsControlIntent.Action.RIGHT, Input.Keys.D);
        physicsControlMap.put(PhysicsControlIntent.Action.ROTATE_LEFT, Input.Keys.Q);
        physicsControlMap.put(PhysicsControlIntent.Action.ROTATE_RIGHT, Input.Keys.E);


        actionControlMap = new ObjectMap<ActionControlIntent.Action, Integer>();

        //define action key bindings
        actionControlMap.put(ActionControlIntent.Action.LUNGE, Input.Keys.SHIFT_LEFT);
        actionControlMap.put(ActionControlIntent.Action.GRAB, Input.Keys.SPACE);

    }

    @Override
    public IntentRegistry<ActionControlIntent> getActionIntentRegistry() {
        return actionIntentTrackers;
    }

    @Override
    public IntentRegistry<PhysicsControlIntent> getPhysicsIntentRegistry() {
        return physicsIntentTrackers;
    }

    @Override
    public void pollIntents() {

        ObjectMap.Keys<PhysicsControlIntent.Action> physControls = physicsControlMap.keys();

        while (physControls.hasNext()){
            PhysicsControlIntent.Action action = physControls.next();
            int keycode = physicsControlMap.get(action);

            if(Gdx.input.isKeyPressed(keycode)){
                PhysicsControlIntent pci = new PhysicsControlIntent(action, 1);
                physicsIntentTrackers.notifyTrackers(pci, controllerId);
            }

        }

        ObjectMap.Keys<ActionControlIntent.Action> actionControls = actionControlMap.keys();

        while (actionControls.hasNext()){
            ActionControlIntent.Action action = actionControls.next();
            int keycode = actionControlMap.get(action);

            if(Gdx.input.isKeyPressed(keycode)){
                ActionControlIntent aci = new ActionControlIntent(action, 1);
                actionIntentTrackers.notifyTrackers(aci, controllerId);
            }

        }

    }
}
