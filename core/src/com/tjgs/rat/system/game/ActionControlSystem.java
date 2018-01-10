package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.Physics;
import com.tjgs.rat.component.game.ActionController;
import com.tjgs.rat.input.intent.ActionControlIntent;
import com.tjgs.rat.input.intent.IntQueueMap;
import com.tjgs.rat.input.intent.IntentTracker;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class ActionControlSystem extends IteratingSystem implements IntentTracker<ActionControlIntent>{

    @SuppressWarnings("unused")
    private ComponentMapper<ActionController> mActionControl;

    @SuppressWarnings("unused")
    private ComponentMapper<Physics> mPhysics;

    private IntQueueMap<ActionControlIntent> intents;

    @SuppressWarnings("unchecked")
    public ActionControlSystem(){
        super(Aspect.all(ActionController.class));
    }

    @Override
    protected void initialize() {
        intents = new IntQueueMap<ActionControlIntent>();
    }

    @Override
    public void intentCreated(ActionControlIntent intent, int controllerId) {
        intents.add(controllerId, intent);
    }

    @Override
    protected void process(int entityId) {

        ActionController controller = mActionControl.get(entityId);

        Queue<ActionControlIntent> actionIntents = intents.getQueue(controller.controllerId);

        if(actionIntents == null || actionIntents.size <= 0){
            //no actions we are done here
            return;
        }

        for (ActionControlIntent intent: intents.getQueue(controller.controllerId)) {
            switch (intent.getIntent()) {
                case LUNGE:
                    if(mPhysics.has(entityId)) {
                        lunge(entityId, intent.getScale());
                    }
                    break;
                case GRAB:
                    //grab(entityId, intent.getScale());
                    break;
            }
        }
    }

    private void lunge(int entityId, float scale){
        ActionController controller = mActionControl.get(entityId);
        Physics physics = mPhysics.get(entityId);

        if(System.currentTimeMillis() - controller.lastLunge > controller.lungeCoolDown) {
            Body body = physics.getBody();
            Vector2 dir = new Vector2(0, 1);
            dir.rotateRad(body.getAngle());

            dir.scl(controller.lungeForwardSpeed * scale);
            body.applyLinearImpulse(dir, body.getPosition(), true);

            controller.lastLunge = System.currentTimeMillis();
        }
    }

    //private void grab(int entityId, float scale){ }

    @Override
    protected void end() {
        intents.clearValues();
    }
}
