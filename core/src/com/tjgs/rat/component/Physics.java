package com.tjgs.rat.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.builder.physics.BodyBuilder;
import com.tjgs.rat.data.Dependency;
import com.tjgs.rat.data.physics.BodyData;

/**
 * Created by Tyler Johnson on 4/2/2017.
 *
 */

public class Physics extends SerializableComponent<Physics> {

    private static final String TAG = Physics.class.getSimpleName();

    protected String bodyBuilderDependency = "";
    protected BodyData bodyData;

    protected transient boolean existsInSimulation;
    protected transient Body body;

    public Physics(){
        existsInSimulation = false;
        body = null;
    }

    public Body getBody() {
        return body;
    }

    public BodyData getBodyData() {
        return bodyData;
    }

    public void setBodyData(BodyData bodyData) {

        if(bodyData == null){
            Gdx.app.log(TAG, "Setting bodyData to null value");
        }

        this.bodyData = bodyData;
    }

    public boolean existsInSimulation() {
        return existsInSimulation;
    }

    public void bodyCreated(Body body){
        this.body = body;

        if(body != null){
            existsInSimulation = true;
        }
    }

    public void bodyDestroyed(){
        this.body = null;
        existsInSimulation = false;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        bodyData = Dependency.getDependency(bodyBuilderDependency, bodyData,
                BodyData.class, dependencyMap, TAG);

        if(bodyData != null) {
            bodyData.linkDependencies(dependencyMap);
        }
    }

    @Override
    public void copyAndBuild(Physics physics) {
        this.bodyData = physics.bodyData;
    }

    @Override
    public void preSerialize() {
        Gdx.app.log(TAG, "Physics body state not copied before save");

        //Null is being passed to body builder because it is only being used for copy
        //functionality, this should be the only case where this happens.
        BodyBuilder bodyBuilder = new BodyBuilder(null);

        //copy body state
        bodyBuilder.copyState(body, bodyData);
    }

    @Override
    public boolean isValid() {

        if(bodyData == null){
            Gdx.app.log(TAG, "Invalid, bodyData is null");
            return false;
        }

        if(!bodyData.isValid()){
            Gdx.app.log(TAG, "bodyData invalid");
            return false;
        }

        return true;
    }
}
