package com.tjgs.rat.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Tyler Johnson on 4/18/2017.
 *
 */

public abstract class DependencyDataObject implements DataObject {

    protected Array<Dependency> dependencies;

    protected transient ObjectMap<String, Dependency> dependencyMap;

    public DependencyDataObject(){
        dependencies = new Array<Dependency>();
        dependencyMap = new ObjectMap<String, Dependency>();
    }

    public void prepareDependencies(){
        //add all dependencies to map
        for(Dependency dependency: dependencies){
            if(dependency != null) { //can't add null dependency
                dependencyMap.put(dependency.dependencyName, dependency);
            }
        }

        //initiate the builders linking process
        this.linkDependencies(dependencyMap);

    }

}
