package com.tjgs.rat.builder.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.data.physics.BodyData;
import com.tjgs.rat.data.physics.FixtureData;
import com.tjgs.rat.util.physics.PhysicsWorld;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class BodyBuilder implements Builder<Body, BodyData> {

    private PhysicsWorld physicsWorld;
    private FixtureBuilder fixtureBuilder;

    public BodyBuilder(PhysicsWorld physicsWorld){
        this.physicsWorld = physicsWorld;
        fixtureBuilder = new FixtureBuilder();
    }

    @Override
    public Body build(BodyData data) {

        Body body = physicsWorld.world.createBody(data.getBodyDef());

        for(FixtureData fixtureData : data.getFixtures()){
            body.createFixture(fixtureBuilder.build(fixtureData));
        }

        return body;
    }

    @Override
    public void copyState(Body body, BodyData bodyData) {

        bodyData.getFixtures().clear();

        //copy fixtures of the body
        for(Fixture fixture: body.getFixtureList()){
            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.friction = fixture.getFriction();
            fixtureDef.restitution = fixture.getRestitution();
            fixtureDef.density = fixture.getDensity();
            fixtureDef.isSensor = fixture.isSensor();
            fixtureDef.shape = fixture.getShape();

            FixtureData fixtureData = new FixtureData();
            fixtureBuilder.copyState(fixtureDef, fixtureData);

            bodyData.getFixtures().add(fixtureData);
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = body.getType();
        bodyDef.position.set(body.getPosition());
        bodyDef.angle = body.getAngle();

        bodyDef.linearVelocity.set(body.getLinearVelocity());
        bodyDef.angularVelocity = body.getAngularVelocity();

        bodyDef.linearDamping = body.getLinearDamping();
        bodyDef.angularDamping = body.getAngularDamping();

        bodyDef.allowSleep = body.isSleepingAllowed();
//        bodyDef.awake = body.isAwake();//potential decrease in error with this not serialized
        bodyDef.fixedRotation = body.isFixedRotation();
        bodyDef.bullet = body.isBullet();
        bodyDef.active = body.isActive();

        bodyDef.gravityScale = body.getGravityScale();

        bodyData.setBodyDef(bodyDef);

    }
}
