package com.tjgs.rat.data.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.util.VertexBuilder;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/14/2017.
 *
 */
public class MeshData implements DataObject {

    private static final String TAG = MeshData.class.getSimpleName();

    public static final int VERT_SIZE = 2;
    public static final int TEX_COORD_SIZE = 2;
    public static final int COLOR_SIZE = 4;

    protected String vertexDependency = "";
    protected float[] vertices; //should never be null OR have 0 elements

    protected short[] indices; //should never be null OR have 0 elements
    protected float[] colors; // (colors.length / COLOR_SIZE) - (vertices.length / VERT_SIZE) should be >= 0
    protected float[] textureCoords; // same condition as above but fot texture not color


    public int getNumVertices(){
        if(vertices == null){
            return 0;
        }
        return vertices.length/VERT_SIZE;
    }

    public int getNumIndices(){
        if(indices == null){
            return 0;
        }
        return indices.length;
    }

    /**
     * if textureCoords exists (not null, array length is 1 or more)
     */
    public boolean isUsingTexture(){

        if(textureCoords != null){
            return textureCoords.length > 0;
        }
        return false;
    }

    /**
     * if colors exists (not null, array length is 1 or more)
     */
    public boolean isUsingColor(){
        if(colors != null){
            return colors.length > 0;
        }
        return false;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        VertexBuilder data = Dependency.getDependency(vertexDependency, null, VertexBuilder.class, dependencyMap, TAG);

        //System.out.println("Mesh linkage");

        if(data != null){
            //System.out.println("found verts " + vertexDependency + " with num verts " + data.getVertices().length);
            this.vertices = data.getVertices();
        }
    }

    @Override
    public boolean isValid() {
        if(vertices == null){
            Gdx.app.log(TAG, "Invalid mesh, vertices are required");
            return false;
        }

        if(indices == null){
            Gdx.app.log(TAG, "Invalid mesh, indices are required");
            return false;
        }

        if(vertices.length % VERT_SIZE != 0){
            Gdx.app.log(TAG, "Invalid mesh, each vertex need should have " + VERT_SIZE +
                    " values. A vertex is missing " + vertices.length % VERT_SIZE + "values");
            return false;
        }

        int numVerts = vertices.length / VERT_SIZE;

        boolean useTex = isUsingTexture();
        boolean useColor = isUsingColor();

        if(useTex){
            int numTexVerts = textureCoords.length / TEX_COORD_SIZE;
            //if there aren't enough texture coords defined
            if((numTexVerts - numVerts) < 0){
                Gdx.app.log(TAG, "Invalid mesh, not enough texture coords defined for vertices");
                return false;
            }
        }

        if(useColor){
            int numColorVerts = colors.length / COLOR_SIZE;
            //if there aren't enough colors defined
            if((numColorVerts - numVerts) < 0){
                Gdx.app.log(TAG, "Invalid mesh, each vertex needs a color. Not enough colors defined");
                return false;
            }
        }

        return true;
    }

    public final float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        if(vertices == null){
            Gdx.app.log(TAG, "Setting vertices to null value!");
        }

        this.vertices = vertices;
    }

    public final short[] getIndices() {
        return indices;
    }

    public void setIndices(short[] indices) {
        if(indices == null){
            Gdx.app.log(TAG, "Setting indices to null value!");
        }

        this.indices = indices;
    }

    public final float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
        if(colors == null){
            Gdx.app.log(TAG, "Setting colors to null value!");
        }
    }

    public final float[] getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
        if(textureCoords == null){
            Gdx.app.log(TAG, "Setting textureCoords to null value!");
        }
    }
}
