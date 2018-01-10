package com.tjgs.rat.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.builder.graphics.MeshBuilder;
import com.tjgs.rat.data.graphics.MeshData;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/14/2017.
 *
 */
public class MeshGraphics extends SerializableComponent<MeshGraphics> {

    private static final String TAG = MeshGraphics.class.getSimpleName();

    protected String meshBuilderDependency;
    protected MeshData meshData;

    public float xScale = 1;
    public float yScale = 1;

    public String textureName = "default.png";

    protected transient Mesh mesh;
    protected transient Texture texture;
    protected transient boolean useTexture;

    public final MeshData getMeshData() {
        return meshData;
    }

    public void setMeshData(MeshData meshData) {
        if(meshData != null) {
            this.meshData = meshData;
        }else{
            Gdx.app.log(TAG, "Tried to set MeshData to null, MeshData not set");
        }
    }

    public int getNumVertices(){
        return meshData.getNumVertices();
    }

    public int getNumInidces(){
        return meshData.getNumIndices();
    }

    public final Mesh getMesh() {
        return mesh;
    }

    public final Texture getTexture() {
        return texture;
    }

    public boolean useTexture() {
        return useTexture;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {

        meshData = Dependency.getDependency(meshBuilderDependency, meshData,
                MeshData.class, dependencyMap, TAG);

        if(meshData != null){
            meshData.linkDependencies(dependencyMap);
        }

    }

    @Override
    public void copyAndBuild(MeshGraphics component) {
        MeshBuilder builder = new MeshBuilder();

        this.meshData = component.meshData;
        this.mesh = builder.build(meshData);
        this.textureName = component.textureName;
        this.xScale = component.xScale;
        this.yScale = component.yScale;

        useTexture = meshData.isUsingTexture();

        if(useTexture){
            texture = new Texture(textureName);
        }
    }

    @Override
    public void preSerialize() {
        MeshBuilder builder = new MeshBuilder();
        builder.copyState(mesh, meshData);
    }

    public boolean isValid() {

        if(!meshData.isValid()){
            return false;
        }

        if(!Gdx.files.internal(textureName).exists()){
            Gdx.app.log(TAG, "Invalid state, texture couldn't be found: " + textureName);
            return false;
        }

        return true;
    }

}
