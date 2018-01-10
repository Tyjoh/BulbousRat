package com.tjgs.rat.builder.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.data.physics.ShapeData;

/**
 * Created by Tyler Johnson on 4/23/2017.
 *
 */
public class ShapeBuilder implements Builder<Shape, ShapeData> {

    private static final String TAG = ShapeBuilder.class.getSimpleName();

    @Override
    public Shape build(ShapeData data) {

        assert data != null;

        switch (data.getShapeType()){
            case Polygon:

                PolygonShape polygonShape = new PolygonShape();
                polygonShape.set(data.getVertices());
                return polygonShape;

            case Circle:

                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(data.getRadius());
                circleShape.setPosition(data.getPosition());
                return circleShape;

            default:
                Gdx.app.log(TAG, "Can't build shape with unsupported shape type: " + data.getShapeType());
                assert false;
                return null;
        }

    }

    @Override
    public void copyState(Shape shape, ShapeData data) {

        assert shape != null;
        assert data != null;

        switch (shape.getType()){
            case Polygon:
                setShape((PolygonShape) shape, data);
                return;

            case Circle:

                setShape((CircleShape) shape, data);
                return;

            case Chain:
            case Edge:
                Gdx.app.log(TAG, "Can't copy shape with unsupported shape type: " + data.getShapeType());

            default:
                return;
        }
    }

    private void setShape(PolygonShape shape, ShapeData data){

        float[] vertices = new float[shape.getVertexCount() * 2];

        //copy vertices from shape
        Vector2 vert = new Vector2();
        for (int i = 0; i < vertices.length; i += 2){
            shape.getVertex(i, vert);
            vertices[i] = vert.x;
            vertices[i+1] = vert.y;
        }

        data.setVertices(vertices);
        data.setShapeType(shape.getType());
        data.setRadius(shape.getRadius());
    }

    private void setShape(CircleShape shape, ShapeData data){
        data.setVertices(new float[]{});
        data.setShapeType(shape.getType());
        data.setRadius(shape.getRadius());
    }

}
