package com.tjgs.rat.data.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.data.DataObject;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/10/2017.
 *
 */

public class SpriteData implements DataObject {

    private static final String TAG = SpriteData.class.getSimpleName();

    public String name;
    public float width = 1;
    public float height = 1;
    public final Vector2 origin = new Vector2();
    public String texture = "default.png";
    protected Color color = Color.WHITE;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if(color != null) {
            this.color = color;
        }else{
            Gdx.app.log(TAG, "Cannot assign null color");
        }
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) { }

    @Override
    public boolean isValid() {
        //no negative width or height
        if(width < 0){
            return false;
        }

        if(height < 0){
            return false;
        }

        //no null objects
        if(origin == null){
            return false;
        }

        if(color == null){
            return false;
        }

        //if texture doesn't exist, sprite is not buildable
        if(!Gdx.files.internal(texture).exists()){
            Gdx.app.log(TAG, "Texture cannot be found: " + texture);
            return false;
        }

        return true;
    }
}
