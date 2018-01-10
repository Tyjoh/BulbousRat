package com.tjgs.rat.data;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public interface DataObject {

    void linkDependencies(ObjectMap<String, Dependency> dependencyMap);
    boolean isValid();

}
