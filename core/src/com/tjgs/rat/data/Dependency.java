package com.tjgs.rat.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Tyler Johnson on 4/18/2017.
 *
 */
public class Dependency {

    private static final String TAG = Dependency.class.getSimpleName();

    protected String dependencyName = "";
    protected Object object;

    public boolean validateType(Class clazz){
        return clazz.isAssignableFrom(object.getClass());
    }

    /**
     * Casts and returns the object. If the object is not the correct class type null is returned.
     * @param clazz expected class type of the object
     * @param <T> class type of the object
     * @return casted object
     */
    public <T> T getObject(Class<T> clazz){
        if(validateType(clazz)){
            return (T) object;
        }else{
            return null;
        }
    }

    /**
     * Convenience method used to retrieve and cast a dependency. This function will also
     * Check all error cases and log various erroneous scenarios to make debugging easier.
     * If a dependency cannot be found and the original field value is not null, the original
     * field value will be returned.
     *
     * @param name the name of the dependency
     * @param t the field that is being assigned the dependency
     * @param clazz the class type of the dependency
     * @param dependencyMap the dependency map to load the dependency from
     * @param tag the class tag of the calling class
     * @param <T> the class of the dependency
     * @return dependency object if it can be found and created, null otherwise.
     */
    public static <T> T getDependency(String name, T t, Class<T> clazz, ObjectMap<String, Dependency> dependencyMap, String tag){

        if(name == null){
            return t;
        }

        //if no dependency name is defined
        if(name.length() <= 0 ){
            if(t == null) {//if the dependent field is null
//                Gdx.app.log(tag, "No " + clazz.getSimpleName() + " or dependency defined, field will be null");
                return null;//can't create dependency
            }else{//if the dependent field has a value
                return t;//return itself
            }
        }

        //if there is a dependency name
        if(name.length() > 0){

            //if the dependency exists
            T castedT = null;

            //if the dependency exits
            if(dependencyMap.containsKey(name)) {
                Dependency bbDependency = dependencyMap.get(name);
                castedT = bbDependency.getObject(clazz);
            } else {
                //dependency doesn't exist
                Gdx.app.log(tag, " dependency  \"" + name + "\" not found for " + TAG + " class");
                return t;//return original value
            }

            //notify that the old field value is going to be overwritten
            if(t != null && castedT != null){
                //Gdx.app.log(tag, clazz.getSimpleName() + " already exists, overriding with dependency");
                return castedT;
            }

            //regular case, no logging needed everything is correct here
            if(castedT != null){
                return castedT;
            }

            //if both are null the dependency couldn't be created because types were incorrect
            if(t == null){
                Gdx.app.log(tag, " dependency \"" + name + "\" for" + TAG + " class not correct type, field will be set to null");
                return null;
            }

        }

        return t;// return original value

    }

}
