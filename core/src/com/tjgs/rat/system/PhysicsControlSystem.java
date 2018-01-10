package com.tjgs.rat.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.Physics;
import com.tjgs.rat.component.PhysicsController;
import com.tjgs.rat.input.intent.IntQueueMap;
import com.tjgs.rat.input.intent.IntentTracker;
import com.tjgs.rat.input.intent.PhysicsControlIntent;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class PhysicsControlSystem extends IteratingSystem implements IntentTracker<PhysicsControlIntent>{

    @SuppressWarnings("unused")
    private ComponentMapper<Physics> mPhysics;

    @SuppressWarnings("unused")
    private ComponentMapper<PhysicsController> mPhysControl;

    private IntQueueMap<PhysicsControlIntent> intents;

    @SuppressWarnings("unchecked")
    public PhysicsControlSystem(){
        super(Aspect.all(PhysicsController.class, Physics.class));
    }

    @Override
    protected void initialize() {
        intents = new IntQueueMap<PhysicsControlIntent>();
    }

    @Override
    public void intentCreated(PhysicsControlIntent intent, int controllerId) {
        intents.add(controllerId, intent);
    }

    @Override
    protected void process(int entityId) {
        Physics physics = mPhysics.get(entityId);
        Body body = physics.getBody();
        PhysicsController controller = mPhysControl.get(entityId);
        //get list of intents that this entity is subscribed to

        //represents linear and angular direction, as well as scale of speed/acceleration
        Vector2 moveDir = new Vector2();
        float rotDir = 0;

        Queue<PhysicsControlIntent> intentQueue = intents.getQueue(controller.controllerId);

        if(intentQueue == null || intentQueue.size <= 0){
            //if no intents exist we are done here
            return;
        }

        for (PhysicsControlIntent intent: intents.getQueue(controller.controllerId)){
            switch (intent.getIntent()){
                case FORWARD:
                    moveDir.y += intent.getScale();
                    break;
                case BACKWARD:
                    moveDir.y -= intent.getScale();
                    break;
                case LEFT:
                    moveDir.x -= intent.getScale();
                    break;
                case RIGHT:
                    moveDir.x += intent.getScale();
                    break;
                case ROTATE_LEFT:
                    rotDir += intent.getScale();
                    break;
                case ROTATE_RIGHT:
                    rotDir -= intent.getScale();
                    break;
            }
        }

        applyLinearImpulse(body, moveDir, controller);

        applyAngularImpulse(body, rotDir, controller);
        
    }

    @Override
    protected void end() {
        intents.clearValues();
    }

    private void applyLinearImpulse(Body body, Vector2 moveDir, PhysicsController controller){
        if(moveDir.len() > 1){
            moveDir.nor();
        }

        //rotate relative to where the body is facing
        moveDir.rotateRad(body.getAngle());

        //scale to acceleration
        moveDir.scl(controller.linearAccel);

        //apply an impulse on the body
        body.applyLinearImpulse(moveDir, body.getPosition(), true);

        //TODO: test that this affects the actual velocity, may be better practice to copy velocity and use set() instead
        //limit body's velocity to its maximum speed
        float speedOver = body.getLinearVelocity().len() - controller.maxLinearSpeed;
        if(speedOver > 0){
            Vector2 dir = body.getLinearVelocity().cpy();
            dir.nor();
            dir.scl(-controller.linearAccel);
            dir.clamp(0, speedOver);//don't slow down lower than min speed
            body.applyLinearImpulse(dir, body.getPosition(), true);
        }

        //Vector2 linVel = body.getLinearVelocity().clamp(0, controller.maxLinearSpeed);

    }
    
    private void applyAngularImpulse(Body body, float rotDir, PhysicsController controller){
        //limit rot dir to [1, -1] bounds
        if(rotDir > 1){
            rotDir = 1;
        }else if(rotDir < -1){
            rotDir = -1;
        }

        //scale rotation to angular acceleration
        rotDir = rotDir * controller.angularAccel;

        //apply the angular impulse
        body.applyAngularImpulse(rotDir * body.getInertia(), true);

        //clamp angular speed to maximum speed
        float maxSpeed = controller.maxAngularSpeed;

        if(body.getAngularVelocity() > maxSpeed){
            body.setAngularVelocity(maxSpeed);
        }else if (body.getAngularVelocity() < -maxSpeed){
            body.setAngularVelocity(-maxSpeed);
        }
    }
}
