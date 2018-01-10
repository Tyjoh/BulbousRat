package com.tjgs.rat.component;

import com.artemis.Component;
import com.tjgs.rat.data.DataObject;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public abstract class SerializableComponent<T> extends Component implements DataObject {

    public abstract void copyAndBuild(T component);
    public abstract void preSerialize();

}
