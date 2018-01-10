package com.tjgs.rat.data.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/10/2017.
 *
 */

public class BodyData implements DataObject {

    private static final String TAG = BodyData.class.getSimpleName();

    private BodyDef bodyDef;
    private Array<FixtureData> fixtures;

    public BodyData(){
        bodyDef = new BodyDef();
        fixtures = new Array<FixtureData>();
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        assert bodyDef != null;

        this.bodyDef = bodyDef;
    }

    public Array<FixtureData> getFixtures() {
        return fixtures;
    }

    public void setFixtures(Array<FixtureData> fixtures) {
        this.fixtures = fixtures;
    }

    public void addFixture(FixtureData fixtureData){
        this.fixtures.add(fixtureData);
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        for(FixtureData fixture: fixtures){
            fixture.linkDependencies(dependencyMap);
        }
    }

    @Override
    public boolean isValid() {

        for(FixtureData fixtureData : fixtures){
            if(!fixtureData.isValid()){
                Gdx.app.log(TAG, "Fixture builder invalid: " + fixtureData.name);
                return false;
            }
        }

        return true;
    }

}
