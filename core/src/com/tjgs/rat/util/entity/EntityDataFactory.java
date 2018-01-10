package com.tjgs.rat.util.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityGroupData;
import com.tjgs.rat.data.entity.EntityPrefabData;
import com.tjgs.rat.util.JsonCreator;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public class EntityDataFactory {

    private static final String TAG = EntityDataFactory.class.getSimpleName();

    private static final String path = "entities/";

    private Json json;

    public EntityDataFactory(){
        json = JsonCreator.getJson();
    }

    public EntityData getEntityBuilder(String name){
        String pathName = path + name + ".json";
        FileHandle file = Gdx.files.internal(pathName);

        if(!file.exists()){
            Gdx.app.log(TAG, "EntityData at \"" + pathName + "\" does not exist");
            return null;
        }

        EntityData data = json.fromJson(EntityData.class, file);

        if(data == null){
            Gdx.app.log(TAG, "EntityData \"" + name + "\" could not be parsed");
            return null;
        }

        data.prepareDependencies();
        return data;
    }

    public EntityPrefabData getEntityPrefab(String name){
        String pathName = path + name + ".json";
        FileHandle file = Gdx.files.internal(pathName);

        if(!file.exists()){
            Gdx.app.log(TAG, "EntityPrefabData at \"" + pathName + "\" does not exist");
        }

        EntityPrefabData data = json.fromJson(EntityPrefabData.class, file);

        if(data == null){
            Gdx.app.log(TAG, "EntityPrefabData \"" + name + "\" could not be parsed");
            return null;
        }

        data.prepareDependencies();
        return data;
    }

    public EntityGroupData getEntityGroupBuilder(String name){
        String pathName = path + name + ".json";
        FileHandle file = Gdx.files.internal(pathName);

        if(!file.exists()){
            Gdx.app.log(TAG, "EntityGroupData at \"" + pathName + "\" does not exist");
        }

        EntityGroupData data = json.fromJson(EntityGroupData.class, file);

        if(data == null){
            Gdx.app.log(TAG, "EntityGroupData \"" + name + "\" could not be parsed");
            return null;
        }

        data.prepareDependencies();
        return data;
    }

}
