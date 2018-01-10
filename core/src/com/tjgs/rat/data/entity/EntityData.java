package com.tjgs.rat.data.entity;

import com.artemis.ArchetypeBuilder;
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
public class EntityData extends DependencyDataObject implements DataObject {

    private static final String TAG = EntityData.class.getSimpleName();

    public String prefabName = "";

    public String name;
    public final Array<SerializableComponent> components;
    public final Array<String> componentDependencies;

    public EntityData(){
        components = new Array<SerializableComponent>();
        componentDependencies = new Array<String>();
    }

    public boolean useEntityPrefab(){
        return prefabName != null && !prefabName.equals("");
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        //add dependent components
        for(String dependentComponent: componentDependencies){
            SerializableComponent component = null;
            component = Dependency.getDependency(dependentComponent, component, SerializableComponent.class, dependencyMap, TAG);
            components.add(component);
        }

        //link all components
        for(SerializableComponent component: components){
            component.linkDependencies(dependencyMap);
        }
    }

    @Override
    public boolean isValid() {

        for(SerializableComponent component: components){
            if(!component.isValid()){
                return false;
            }
        }

        return true;
    }

    public void preSerialize(){
        for(SerializableComponent component: components){
            component.preSerialize();
        }
    }

    public ArchetypeBuilder getArchetypeBuilder(){
        ArchetypeBuilder builder = new ArchetypeBuilder();

        for (SerializableComponent component: components){
            builder.add(component.getClass());
        }

        return builder;
    }

}
