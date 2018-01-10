package com.tjgs.rat.builder.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tjgs.rat.builder.Builder;
import com.tjgs.rat.data.graphics.SpriteData;

/**
 * Created by Tyler Johnson on 4/23/2017.
 * 
 */
public class SpriteBuilder implements Builder<Sprite, SpriteData> {

    @Override
    public Sprite build(SpriteData data) {

        Texture tex = new Texture(Gdx.files.internal(data.texture));

        Sprite sprite = new Sprite(tex);
        sprite.setColor(data.getColor());
        sprite.setSize(data.width, data.height);
        sprite.setOrigin(data.origin.x, data.origin.y);

        return sprite;
    }

    @Override
    public void copyState(Sprite sprite, SpriteData data) {
        data.width = sprite.getWidth();
        data.height = sprite.getHeight();
        data.origin.set(sprite.getOriginX(), sprite.getOriginY());
        data.setColor(sprite.getColor());
    }
    
}
