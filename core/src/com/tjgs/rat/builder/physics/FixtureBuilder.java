package com.tjgs.rat.builder.physics;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.data.physics.FixtureData;
import com.tjgs.rat.data.physics.ShapeData;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class FixtureBuilder implements Builder<FixtureDef, FixtureData> {

    private ShapeBuilder shapeBuilder;

    public FixtureBuilder(){
        shapeBuilder = new ShapeBuilder();
    }

    @Override
    public FixtureDef build(FixtureData fixtureData) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = fixtureData.friction;
        fixtureDef.restitution = fixtureData.restitution;
        fixtureDef.density = fixtureData.density;
        fixtureDef.isSensor = fixtureData.isSensor;

        fixtureDef.shape = shapeBuilder.build(fixtureData.getShapeData());

        return fixtureDef;
    }

    @Override
    public void copyState(FixtureDef fixtureDef, FixtureData fixtureData) {

        //copy shape
        fixtureData.setShape(new ShapeData());
        shapeBuilder.copyState(fixtureDef.shape, fixtureData.getShapeData());

        fixtureData.friction = fixtureDef.friction;
        fixtureData.restitution = fixtureDef.restitution;
        fixtureData.density = fixtureDef.density;
        fixtureData.isSensor = fixtureDef.isSensor;
    }
}
