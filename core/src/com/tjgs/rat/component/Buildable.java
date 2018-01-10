package com.tjgs.rat.component;

/**
 * Created by Tyler Johnson on 4/13/2017.
 *
 */
public interface Buildable <T> {

   void copyAndBuild(T t);
   void preSerialize();

}
