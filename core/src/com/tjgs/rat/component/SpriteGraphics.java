package com.tjgs.rat.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;
import com.tjgs.rat.builder.graphics.SpriteBuilder;
import com.tjgs.rat.data.graphics.SpriteData;
import com.tjgs.rat.data.Dependency;

/**
 * Created by Tyler Johnson on 4/2/2017.
 *
 */

public class SpriteGraphics extends SerializableComponent<SpriteGraphics> {

    private static final String TAG = SpriteGraphics.class.getSimpleName();

    protected String spriteBuilderDependency = "";
    protected SpriteData spriteData;

    transient private Sprite sprite;

    public Sprite getSprite() {
        return sprite;
    }

    public SpriteData getSpriteData() {
        return spriteData;
    }

    public void setSpriteData(SpriteData spriteData) {
        if(spriteData == null){
            Gdx.app.log(TAG, "setting spriteData to null value");
        }
        this.spriteData = spriteData;
    }

    @Override
    public void linkDependencies(ObjectMap<String, Dependency> dependencyMap) {

        spriteData = Dependency.getDependency(spriteBuilderDependency, spriteData,
                SpriteData.class, dependencyMap, TAG);

    }

    @Override
    public void copyAndBuild(SpriteGraphics spriteGraphics) {
        SpriteBuilder builder = new SpriteBuilder();
        this.spriteData = spriteGraphics.spriteData;
        this.sprite = builder.build(spriteData);
    }

    @Override
    public void preSerialize() {
        SpriteBuilder builder = new SpriteBuilder();
        builder.copyState(sprite, spriteData);
    }

    @Override
    public boolean isValid() {

        if(spriteData == null){
            Gdx.app.log(TAG, "Invalid, spriteData is null");
            return false;
        }

        return spriteData.isValid();
    }
}
