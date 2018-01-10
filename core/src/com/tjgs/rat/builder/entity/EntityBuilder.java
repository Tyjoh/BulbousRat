package com.tjgs.rat.builder.entity;

import com.artemis.Archetype;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityPrefabData;
import com.tjgs.rat.util.entity.EntityDataFactory;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class EntityBuilder implements Builder<Integer, EntityData> {

    private static final String TAG = EntityBuilder.class.getSimpleName();

    private World world;
    private EntityDataFactory factory;

    public EntityBuilder(World world) {
        this.world = world;
        factory = new EntityDataFactory();
    }

    @Override
    public Integer build(EntityData data) {

        Archetype archetype = data.getArchetypeBuilder().build(world);
        int entityId = world.create(archetype);//creates entity with the defined entities components, not the prefab.

        //if the entity uses a prefab add all prefab elements. This is so that any components in the prefab
        //will overwrite those with those in the full entity definition.
        if(data.useEntityPrefab()){

            EntityPrefabData prefabData = factory.getEntityPrefab(data.prefabName);

            if(prefabData == null){
                return -1;//logging is already done in factory
            }

            if(!hasRequiredComponents(data, prefabData)) {
                Gdx.app.log(TAG, "Entity Prefab could not be loaded, entity not being built");
                return -1;
            }

            for(SerializableComponent component: prefabData.getPrefabBuilder().components){
                SerializableComponent worldComponent = world.getMapper(component.getClass()).create(entityId);

                worldComponent.copyAndBuild(component);
            }
        }


        //initialize all components to that of the builder
        for(SerializableComponent component: data.components){
            SerializableComponent worldComponent = world.getMapper(component.getClass()).get(entityId);

            worldComponent.copyAndBuild(component);
        }

        return entityId;
    }

    private boolean hasRequiredComponents(EntityData data, EntityPrefabData prefabData){

        //TODO: make more efficient, currently O(N^2) time, could potentially be O(1) ~ O(N)
        //iterate over all required component names.
        for(String requiredName: prefabData.getRequiredComponents()){
            boolean hasComponent = false;
            //find whether prefab builder has the required component defined
            for(SerializableComponent component: data.components){
                if(component.getClass().getSimpleName().equals(requiredName)){
                    hasComponent = true;
                    break;
                }
            }

            //if it doesn't have a required component return invalid entityId
            if(!hasComponent){
                Gdx.app.log(TAG, "Entity \"" + data.name + "\" does not define required component \""
                        + requiredName + "\" for prefab \"" + data.prefabName + "\"");
                return false;
            }
        }

        return true;
    }

    @Override
    public void copyState(Integer integer, EntityData entityData) {
        Gdx.app.log(TAG, "copyState is not implemented");
        //TODO: implement for serialization
    }
}
