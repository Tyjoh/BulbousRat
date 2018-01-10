package com.tjgs.rat.data.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public class JointData implements DataObject {

    private static final String TAG = JointData.class.getSimpleName();

    public String jointDefDependency = "";
    protected JointDef jointDef;

    public int entityA = -1;
    public int entityB = -1;

    public void preSerialize(){ }

    public JointDef getJointDef() {
        return jointDef;
    }

    public void setJointDef(JointDef jointDef) {
        if(jointDef == null){
            Gdx.app.log(TAG, "Setting jointDef to a null value");
        }
        this.jointDef = jointDef;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {

        if(dependencyMap.containsKey(jointDefDependency)){
            Dependency dependency = dependencyMap.get(jointDefDependency);
            if(dependency.validateType(JointDef.class)) {
                jointDef = dependencyMap.get(jointDefDependency).getObject(JointDef.class);
            }
        }
    }

    @Override
    public boolean isValid() {

        if(jointDef == null){
            Gdx.app.log(TAG, "Invalid, jointDef has null value");
            return false;
        }

        if(entityA < 0){
            Gdx.app.log(TAG, "Invalid, entityA not defined");
            return false;
        }

        if(entityB < 0){
            Gdx.app.log(TAG, "Invalid, entityB not defined");
            return false;
        }

        if(entityA == entityB){
            Gdx.app.log(TAG, "Invalid, cannot create joint between same body");
            return false;
        }

        return true;
    }
}
