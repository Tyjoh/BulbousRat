package com.tjgs.rat.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/18/2017.
 *
 */
public interface DependencyLinker {

   void linkDependencies(ObjectMap<String, Dependency> dependencyMap);

}
