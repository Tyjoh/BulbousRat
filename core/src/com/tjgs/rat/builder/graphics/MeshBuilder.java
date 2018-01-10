package com.tjgs.rat.builder.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.data.graphics.MeshData;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class MeshBuilder implements Builder<Mesh, MeshData> {

    private static final String TAG = MeshBuilder.class.getSimpleName();

    @Override
    public Mesh build(MeshData data) {

        boolean useTex = data.isUsingTexture();
        boolean useColor = data.isUsingColor();

        //construct vertex attributes based on the data that is required and/or exists
        Array<VertexAttribute> attribs = new Array<VertexAttribute>();
        attribs.add(new VertexAttribute(VertexAttributes.Usage.Position, MeshData.VERT_SIZE, ShaderProgram.POSITION_ATTRIBUTE));

        int size = MeshData.VERT_SIZE;

        if(useColor){
            attribs.add(new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, MeshData.COLOR_SIZE, ShaderProgram.COLOR_ATTRIBUTE));
            size += MeshData.COLOR_SIZE;
        }

        if(useTex){
            attribs.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, MeshData.TEX_COORD_SIZE, ShaderProgram.TEXCOORD_ATTRIBUTE));
            size += MeshData.TEX_COORD_SIZE;
        }

        float[] vertices = data.getVertices();
        float[] colors = data.getColors();
        float[] textureCoords = data.getTextureCoords();

        //calculate the number of vertices in the mesh based only on vertex array
        int numVertices = vertices.length / MeshData.VERT_SIZE;

        float[] vertexData = new float[size * numVertices];

        //copy data that is being used into the vertexData array
        for(int i = 0; i < numVertices; i++) {
            int vdi = i * size;//vertex data index

            vertexData[vdi++] = vertices[(i * MeshData.VERT_SIZE)];
            vertexData[vdi++] = vertices[(i * MeshData.VERT_SIZE) + 1];
            //vertexData[vdi++] = vertices[(i * VERT_SIZE) + 2];

            if (useColor) {
                vertexData[vdi++] = colors[(i * MeshData.COLOR_SIZE)];
                vertexData[vdi++] = colors[(i * MeshData.COLOR_SIZE) + 1];
                vertexData[vdi++] = colors[(i * MeshData.COLOR_SIZE) + 2];
                vertexData[vdi++] = colors[(i * MeshData.COLOR_SIZE) + 3];
            }

            if (useTex) {
                vertexData[vdi++] = textureCoords[(i * MeshData.TEX_COORD_SIZE)];
                vertexData[vdi++] = textureCoords[(i * MeshData.TEX_COORD_SIZE) + 1];
            }

        }

        //create a attribute array for mesh creation
        VertexAttribute[] attribsArray = new VertexAttribute[attribs.size];

        int i = 0;
        for(VertexAttribute va: attribs){
            attribsArray[i++] = va;
        }

        //create the mesh
        Mesh mesh = new Mesh(true, numVertices, data.getIndices().length, attribsArray);
        mesh.setVertices(vertexData);
        mesh.setIndices(data.getIndices());

        //System.out.println(vertexDependency + " index len: " + indices.length);

        return mesh;
    }

    @Override
    public void copyState(Mesh mesh, MeshData meshData) {
        Gdx.app.log(TAG, "copyState is not implemented for mesh graphics");
    }
}
