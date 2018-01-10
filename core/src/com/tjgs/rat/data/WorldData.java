package com.tjgs.rat.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.entity.EntityData;

/**
 * Created by Tyler Johnson on 4/28/2017.
 *
 */
public class WorldData implements DataObject{

    protected Array<EntityData> entities;

    public WorldData(){
        entities = new Array<EntityData>();
    }

    public Array<EntityData> getEntities(){
        return entities;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() {
        return true;
    }
}
