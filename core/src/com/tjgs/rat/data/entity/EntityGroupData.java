package com.tjgs.rat.data.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;
import com.tjgs.rat.data.DependencyDataObject;
import com.tjgs.rat.data.physics.JointData;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public class EntityGroupData extends DependencyDataObject implements DataObject {

    public String groupName = "";
    public final Array<EntityData> entities;

    public final Array<JointData> physicalJoints;

    public final Transform deltaTransform;

    public EntityGroupData(){
        entities = new Array<EntityData>();
        physicalJoints = new Array<JointData>();
        deltaTransform = new Transform();
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        for(EntityData builder: entities){
            builder.linkDependencies(dependencyMap);
        }

        for(JointData jointData: physicalJoints){
            jointData.linkDependencies(dependencyMap);
        }

    }

    @Override
    public boolean isValid() {

        for(EntityData builder: entities){
            if(!builder.isValid()){
                return false;
            }
        }

        return true;
    }

    public void preSerialize(){
        for(EntityData eb: entities){
            eb.preSerialize();
        }

        for(com.tjgs.rat.data.physics.JointData jb: physicalJoints){
            jb.preSerialize();
        }
    }

}
