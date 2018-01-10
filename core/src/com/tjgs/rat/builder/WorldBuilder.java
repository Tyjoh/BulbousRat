package com.tjgs.rat.builder;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.tjgs.rat.component.SerializableComponent;
import com.tjgs.rat.data.WorldData;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.system.EntitySpawnerSystem;

/**
 * Created by Tyler Johnson on 4/28/2017.
 *
 */
public class WorldBuilder implements Builder<World, WorldData>{

    private World world;
    private EntitySubscription allEntitiesSub;

    public WorldBuilder(World world){
        this.world = world;
        allEntitiesSub = world.getAspectSubscriptionManager().get(Aspect.all());
    }

    @Override
    public World build(WorldData worldData) {
        EntitySpawnerSystem spawnerSystem = world.getSystem(EntitySpawnerSystem.class);
        for(EntityData data: worldData.getEntities()){
            spawnerSystem.queueEntitySpawn(data);
        }

        return world;
    }

    @Override
    public void copyState(World world, WorldData worldData) {
        IntBag entities = allEntitiesSub.getEntities();

        for (int i = 0; i < entities.size(); i++){
            int entityId = entities.get(i);
            worldData.getEntities().add(createEntityData(entityId));
        }
    }

    private EntityData createEntityData(int entityId){

        Bag<Component> components = new Bag<Component>();
        world.getComponentManager().getComponentsFor(entityId, components);

        EntityData data = new EntityData();

        for(int i = 0; i < components.size(); i++){
            Component component = components.get(i);

            if(SerializableComponent.class.isAssignableFrom(component.getClass())){
                SerializableComponent serializableComponent = (SerializableComponent) component;
                data.components.add(serializableComponent);
            }
        }

        return data;
    }
}
