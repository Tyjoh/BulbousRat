package com.tjgs.rat.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.builder.physics.BodyBuilder;
import com.tjgs.rat.component.Physics;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.event.CollisionEvent;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.util.physics.PhysicsWorld;

/**
 * Created by Tyler Johnson on 4/2/2017.
 *
 */

public class PhysicsSystem extends IteratingSystem implements ContactListener{

    private static final String TAG = PhysicsSystem.class.getSimpleName();

    private static final int velocityIterations = 8;
    private static final int positionIterations = 3;
    private static final float timeStep = 1/60f;

    private ComponentMapper<Physics> mPhysics = null;
    private ComponentMapper<Transform> mPosition = null;

    @SuppressWarnings("unused")
    @Wire
    private EventBlackboard eventBlackboard;

    @Wire
    private PhysicsWorld physicsWorld;

    private BodyBuilder bodyBuilder;

    private Queue<JointQueueElement> jointQueue;

    @SuppressWarnings("unchecked")
    public PhysicsSystem() {
        super(Aspect.all(Transform.class, Physics.class));
    }

    @Override
    protected void initialize() {
        bodyBuilder = new BodyBuilder(physicsWorld);
        jointQueue = new Queue<JointQueueElement>();

        physicsWorld.world.setContactListener(this);
    }

    @Override
    protected void removed(int entityId) {
        Physics physics = mPhysics.get(entityId);

        physicsWorld.world.destroyBody(physics.getBody());
        physics.bodyDestroyed();

    }

    @Override
    protected void inserted(int entityId) {
        Physics physics = mPhysics.get(entityId);
        Transform transform = mPosition.get(entityId);

        //check if this object needs to be added to the physics simulation

        if(!physics.existsInSimulation()){

            //set physics to the transform of the transform component
            //this is done to make moving a prefabs transform easier
            physics.getBodyData().getBodyDef().position.set(transform.position);
            physics.getBodyData().getBodyDef().angle = transform.angle;

            Body body = bodyBuilder.build(physics.getBodyData());

            body.setUserData(entityId);

            //add body to simulation
            physics.bodyCreated(body);

            if(physics.getBody() == null){
                Gdx.app.error("PhysicsSystem", "Error creating physics body, body has been excluded from simulation!");
            }
        }
    }

    public void queueJointCreation(JointDef jointDef, int entityAId, int entityBId){
        JointQueueElement jqe = new JointQueueElement();
        jqe.entityAId = entityAId;
        jqe.entityBId = entityBId;
        jqe.jointDef = jointDef;

        jointQueue.addLast(jqe);
    }

    private void createJoints(){

        while (jointQueue.size > 0){
            JointQueueElement joint = jointQueue.removeFirst();

            if(!mPhysics.has(joint.entityAId)){
                Gdx.app.log(TAG, "Could not create joint, entity # \"" + joint.entityAId + "\" doesn't have a physics component");
                continue;
            }

            if(!mPhysics.has(joint.entityBId)){
                Gdx.app.log(TAG, "Could not create joint, entity # \"" + joint.entityBId + "\" doesn't have a physics component");
                continue;
            }

            Physics physicsA = mPhysics.get(joint.entityAId);
            Physics physicsB = mPhysics.get(joint.entityBId);

            Body bodyA = physicsA.getBody();
            Body bodyB = physicsB.getBody();

            joint.jointDef.bodyA = bodyA;
            joint.jointDef.bodyB = bodyB;

            physicsWorld.world.createJoint(joint.jointDef);

        }

    }

    @Override
    protected void begin() {
        //clear last iterations collision events
        eventBlackboard.clearEventType(CollisionEvent.class);

        createJoints();

        //TODO: step at game speed
        physicsWorld.world.step(timeStep, velocityIterations, positionIterations);
    }

    @Override
    protected void process(int entityId) {
        Physics physics = mPhysics.get(entityId);
        Transform transform = mPosition.get(entityId);

        Body body = physics.getBody();
        BodyDef bodyDef = physics.getBodyData().getBodyDef();

        assert body != null;//body should never be null at this point

        //copy body transform back to physics component
        //this will not change the transform of the physics body, used for serialization/deserialization of component
        bodyDef.position.set(body.getPosition());
        bodyDef.angle = body.getAngle();

        //copy physics body transform to transform component
        transform.position.set(body.getPosition());
        transform.angle = body.getAngle();
    }

    @Override
    public void beginContact(Contact contact) {

        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        int bodyAId = -1;
        int bodyBId = -1;

        if(bodyA.getUserData() instanceof Integer){
            bodyAId = (Integer) bodyA.getUserData();
        }

        if(bodyB.getUserData() instanceof Integer){
            bodyBId = (Integer) bodyB.getUserData();
        }

        //true because contact is starting
        CollisionEvent event = new CollisionEvent(contact, bodyAId, bodyBId, true);

        eventBlackboard.addEvent(event, bodyAId);
        eventBlackboard.addEvent(event, bodyBId);

    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        int bodyAId = -1;
        int bodyBId = -1;

        if(bodyA.getUserData() instanceof Integer){
            bodyAId = (Integer) bodyA.getUserData();
        }

        if(bodyB.getUserData() instanceof Integer){
            bodyBId = (Integer) bodyB.getUserData();
        }

        //false because contact is ending
        CollisionEvent event = new CollisionEvent(contact, bodyAId, bodyBId, false);

        eventBlackboard.addEvent(event, bodyAId);
        eventBlackboard.addEvent(event, bodyBId);

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {  }

    private static class JointQueueElement{
        int entityAId;
        int entityBId;
        JointDef jointDef;
    }
}
