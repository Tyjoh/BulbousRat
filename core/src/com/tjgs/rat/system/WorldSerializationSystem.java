package com.tjgs.rat.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.tjgs.rat.builder.WorldBuilder;
import com.tjgs.rat.data.WorldData;
import com.tjgs.rat.util.JsonCreator;

/**
 * Created by Tyler Johnson on 4/28/2017.
 *
 */
public class WorldSerializationSystem extends BaseSystem {

    private static final String worldSavePath = "saves/";

    private WorldBuilder worldBuilder;

    private String worldToLoad;
    private boolean shouldLoadWorld = false;

    @Override
    protected void initialize() {
        this.worldBuilder = new WorldBuilder(world);
    }

    public void loadWorld(String name){
        worldToLoad = name;
        shouldLoadWorld = true;
    }

    @Override
    protected void processSystem() {

        if(shouldLoadWorld){
            FileHandle fileHandle = Gdx.files.local(worldSavePath + worldToLoad + ".json");
            if(!fileHandle.exists()){
                System.out.println("file doesn't exits");
                return;
            }
            Json json = JsonCreator.getJson();

            WorldData data = json.fromJson(WorldData.class, fileHandle.readString());
            worldBuilder.build(data);
            System.out.println("MUTHA FUGGIN SAVE LOADED");

            shouldLoadWorld = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)){
            System.out.println("DO A MUTHA FUGGIN SAVE");

            FileHandle fileHandle = Gdx.files.local(worldSavePath + "quicksave.json");
            Json json = JsonCreator.getJson();

            WorldData data = new WorldData();
            worldBuilder.copyState(world, data);

            String jsonOut = json.prettyPrint(data);
            fileHandle.writeString(jsonOut, false);
        }
    }
}
