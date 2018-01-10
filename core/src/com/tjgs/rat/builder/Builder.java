package com.tjgs.rat.builder;

import com.tjgs.rat.data.DataObject;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 * This interface is used by builder classes that build objects from pure data objects. The data objects are typically
 * serialization friendly
 */
public interface Builder<T, S extends DataObject> {

    //TODO: create javadoc that properly references types T and S

    /**
     * Takes in data object S and assembles a T object. No values in s will change
     * @param s data object to assemble
     * @return assembled object
     */
    T build(S s);

    /**
     * Copies the state of t into the data object s
     * @param t object to copy
     * @param s object to store copy in
     */
    void copyState(T t, S s);

}
