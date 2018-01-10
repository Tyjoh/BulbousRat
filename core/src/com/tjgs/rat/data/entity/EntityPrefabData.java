package com.tjgs.rat.data.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;
import com.tjgs.rat.data.DependencyDataObject;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public class EntityPrefabData extends DependencyDataObject implements DataObject {

    protected Array<String> requiredComponents;
    protected EntityData prefabBuilder;

    public EntityPrefabData(){
        requiredComponents = new Array<String>();
        prefabBuilder = new EntityData();
    }

    public void addRequiredComponent(Class<? extends SerializableComponent> component){
        requiredComponents.add(component.getSimpleName());
    }

    public Array<String> getRequiredComponents(){
        return requiredComponents;
    }

    public void setEntityBuilderPrefab(EntityData builder){
        prefabBuilder = builder;
    }

    public EntityData getPrefabBuilder(){
        return prefabBuilder;
    }

    public void preSerialize(){
        prefabBuilder.preSerialize();
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        prefabBuilder.linkDependencies(dependencyMap);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
