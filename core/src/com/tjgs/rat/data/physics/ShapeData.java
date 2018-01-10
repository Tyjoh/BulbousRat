package com.tjgs.rat.data.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.util.VertexBuilder;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/10/2017.
 *
 */

public class ShapeData implements DataObject {

    private static final String TAG = ShapeData.class.getSimpleName();

    protected String vertexDependency = "";
    private float[] vertices;

    private Shape.Type shapeType = Shape.Type.Circle;
    private float radius = 0.5f;
    private Vector2 position = new Vector2();

    public final Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the position of a circle shape. Transform is only relevant to circle shapes because polygons
     * can just be translated when defining the vertices.
     * @param circlePosition position of a circle shape
     */
    public void setPosition(Vector2 circlePosition) {
        this.position = circlePosition;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] verts){

        assert verts != null;

        vertices = verts;

        shapeType = Shape.Type.Polygon;

    }

    public Shape.Type getShapeType() {
        return shapeType;
    }

    public void setShapeType(Shape.Type shapeType) {
        assert shapeType != null;

        this.shapeType = shapeType;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        assert radius > 0;
        this.radius = radius;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {
        if(shapeType == Shape.Type.Polygon) {
            VertexBuilder data = Dependency.getDependency(vertexDependency, null, VertexBuilder.class, dependencyMap, TAG);

            if (data != null) {
                vertices = data.getVertices();
            }
        }
    }

    @Override
    public boolean isValid() {
        if(!(shapeType == Shape.Type.Polygon || shapeType == Shape.Type.Circle)){
            Gdx.app.log(TAG, "Fixture type not supported: " + shapeType);
            return false;
        }

        if(shapeType == Shape.Type.Polygon && vertices.length < 2){
            Gdx.app.log(TAG, "Not enough vertices defined for a physics shape");
            return false;
        }

        if(shapeType == Shape.Type.Circle && radius <= 0){
            Gdx.app.log(TAG, "Circle has invalid radius (" + radius + ")  valid is a radius > 0");
        }

        //TODO: test winding order

        return true;
    }

    public void copyShape(Shape shape){
        switch (shape.getType()){
            case Polygon:
                copyPolygon((PolygonShape) shape);
                break;
            case Circle:
                copyCircle((CircleShape) shape);
                break;
            case Chain:
            case Edge:
                Gdx.app.log(TAG, "Shape type not serializable " + shape.getType());
        }
    }

    private void copyPolygon(PolygonShape shape){

    }

    private void copyCircle(CircleShape shape){

    }

}
