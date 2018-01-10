package com.tjgs.rat.util;

import com.badlogic.gdx.physics.box2d.joints.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tjgs.rat.component.*;
import com.tjgs.rat.component.game.*;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityGroupData;
import com.tjgs.rat.data.entity.EntityPrefabData;
import com.tjgs.rat.data.graphics.MeshData;
import com.tjgs.rat.data.graphics.SpriteData;
import com.tjgs.rat.data.physics.BodyData;
import com.tjgs.rat.data.physics.FixtureData;
import com.tjgs.rat.data.physics.JointData;
import com.tjgs.rat.data.physics.ShapeData;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public final class JsonCreator {

    /**
     * Array containing classes that are used in serialization.
     * This list is used to simplify the class names for serialization
     */
    private static Class[] serializableClasses = new Class[]{
            //pure data objects
            EntityData.class,
            EntityGroupData.class,
            EntityPrefabData.class,
            MeshData.class,
            SpriteData.class,
            BodyData.class,
            FixtureData.class,
            JointData.class,
            ShapeData.class,
            VertexBuilder.class,

            //components
            SpriteGraphics.class,
            MeshGraphics.class,
            Physics.class,
            PhysicsController.class,
            Transform.class,
            CameraFollower.class,

            //box2d
            RevoluteJointDef.class,
            DistanceJointDef.class,
            PrismaticJointDef.class,
            WeldJointDef.class,
            FrictionJointDef.class,
            RopeJointDef.class,

            //game
            Health.class,
            Damager.class,
            ActionController.class,
            ProximitySensor.class,
            GroupHeal.class,
    };

    private static Json json;
    private static boolean initialized = false;

    public static Json getJson(){
        if(!initialized) {
            initialize();
        }

        return json;
    }

    private static void initialize(){
        initialized = true;
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        for(Class clazz: serializableClasses){
            json.addClassTag(clazz.getSimpleName(), clazz);
        }
    }

}
