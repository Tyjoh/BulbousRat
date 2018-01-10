package com.tjgs.rat.data.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/10/2017.
 *
 */

public class FixtureData implements DataObject {

    private static final String TAG = FixtureData.class.getSimpleName();

    public String name = "";

    /** The friction coefficient, usually in the range [0,1]. **/
    public float friction = 0.2f;

    /** The restitution (elasticity) usually in the range [0,1]. **/
    public float restitution = 0;

    /** The density, usually in kg/m^2. **/
    public float density = 0;

    /** A sensor shape collects contact information but never generates a collision response. */
    public boolean isSensor = false;

    /** Represents the shape of the fixture **/
    private ShapeData shape;

    //TODO: add FilterBuilder support

    public FixtureData(){
        shape = new ShapeData();
    }

    public ShapeData getShapeData() {
        return shape;
    }

    public void setShape(ShapeData shape) {
        assert shape != null;

        this.shape = shape;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        if(shape != null) {
            shape.linkDependencies(dependencyMap);
        }
    }

    @Override
    public boolean isValid() {
        if(!shape.isValid()){
            Gdx.app.log(TAG, "Shape invalid");
            return false;
        }
        return true;
    }
}
