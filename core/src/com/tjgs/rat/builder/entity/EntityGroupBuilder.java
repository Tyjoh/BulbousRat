package com.tjgs.rat.builder.entity;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityGroupData;
import com.tjgs.rat.data.physics.JointData;
import com.tjgs.rat.system.PhysicsSystem;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class EntityGroupBuilder implements Builder<Array<Integer>, EntityGroupData> {

    private static final String TAG = EntityGroupBuilder.class.getSimpleName();

    private EntityBuilder entityBuilder;
    private World world;

    public EntityGroupBuilder(World world){
        this.entityBuilder = new EntityBuilder(world);
        this.world = world;
    }

    @Override
    public Array<Integer> build(EntityGroupData entityGroup){
        return build(entityGroup, new Transform());
    }

    public Array<Integer> build(EntityGroupData entityGroup, Transform delta) {

        Array<Integer> entities = new Array<Integer>(entityGroup.entities.size);

        IntMap<Integer> indexToEntity = new IntMap<Integer>();

        int index = 0;

        for(EntityData entityData : entityGroup.entities){
            int entityId = entityBuilder.build(entityData);

            ComponentMapper<Transform> mTransform = world.getMapper(Transform.class);

            if(mTransform.has(entityId)){
                Transform transform = mTransform.get(entityId);

                transform.position.add(delta.position);
                transform.z += delta.z;
                transform.angle += delta.angle;
            }

            //used when determining what entities to use for physical joints.
            indexToEntity.put(index, entityId);
            index++;
        }

        for(JointData joint: entityGroup.physicalJoints){
            int entityAId = indexToEntity.get(joint.entityA);
            int entityBId = indexToEntity.get(joint.entityB);

            PhysicsSystem physicsSystem = world.getSystem(PhysicsSystem.class);
            physicsSystem.queueJointCreation(joint.getJointDef(), entityAId, entityBId);

        }

        return entities;
    }

    @Override
    public void copyState(Array<Integer> integers, EntityGroupData entityGroupData) {
        Gdx.app.log(TAG, "copyState is not implemented");
        //TODO: implement for serialization
    }
}
