package com.tjgs.rat.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.builder.entity.EntityBuilder;
import com.tjgs.rat.builder.entity.EntityGroupBuilder;
import com.tjgs.rat.component.Physics;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityGroupData;
import com.tjgs.rat.util.entity.EntityDataFactory;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public class EntitySpawnerSystem extends BaseSystem {

    private static final String TAG = EntitySpawnerSystem.class.getSimpleName();

    protected Queue<EntityData> entityData;
    protected EntityBuilder entityBuilder;

    protected Queue<EntityGroupData> entityGroupData;
    protected EntityGroupBuilder entityGroupBuilder;

    protected EntityDataFactory prefabFactory;

    @Override
    protected void initialize() {
        super.initialize();

        entityData = new Queue<EntityData>();
        entityBuilder = new EntityBuilder(world);

        entityGroupData = new Queue<EntityGroupData>();
        entityGroupBuilder = new EntityGroupBuilder(world);

        prefabFactory = new EntityDataFactory();
    }

    public void queueEntitySpawn(EntityData entityData){
        this.entityData.addLast(entityData);
    }

    public void queueEntityGroupSpawn(EntityGroupData entityGroupBuilder){
        entityGroupData.addLast(entityGroupBuilder);
    }

    @Override
    protected void processSystem() {

        while (entityData.size > 0){
            createEntity(entityData.removeFirst());
        }

        while (entityGroupData.size > 0){
            createEntityGroup(entityGroupData.removeFirst());
        }
    }

    protected int createEntity(EntityData entity){

        if(!entity.isValid()){
            Gdx.app.log(TAG, "EntityData invalid, cannot add to world: " + entity.name);
        }

        return entityBuilder.build(entity);

//        //create an entity with all required components
//        Archetype archetype = entity.getArchetypeBuilder().build(world);
//
//        int entityId = world.create(archetype);
//
//        //initialize all components to that of the builder
//        for(SerializableComponent component: entity.components){
//            SerializableComponent worldComponent = world.getMapper(component.getClass()).get(entityId);
//
//            worldComponent.copyAndBuild(component);
//
//        }
//
//        return entityId;
    }

    protected void createEntityGroup(EntityGroupData entityGroup){

        if(!entityGroup.isValid()){
            Gdx.app.log(TAG, "EntityGroupData invalid, cannot add to world: " + entityGroup.groupName);
        }

        entityGroupBuilder.build(entityGroup);

//        IntMap<Integer> indexToEntity = new IntMap<Integer>();
//
//        int index = 0;
//
//        for(EntityData entityData : entityGroup.entities){
//            int entityId = createEntity(entityData);
//            indexToEntity.put(index, entityId);
//            index++;
//        }
//
//        for(JointData joint: entityGroup.physicalJoints){
//            int entityAId = indexToEntity.get(joint.entityA);
//            int entityBId = indexToEntity.get(joint.entityB);
//
//            PhysicsSystem physicsSystem = world.getSystem(PhysicsSystem.class);
//            physicsSystem.queueJointCreation(joint.getJointDef(), entityAId, entityBId);
//
//        }
    }
}
