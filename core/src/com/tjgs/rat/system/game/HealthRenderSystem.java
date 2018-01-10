package com.tjgs.rat.system.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tjgs.rat.component.MeshGraphics;
import com.tjgs.rat.component.SpriteGraphics;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.component.game.Health;
import com.tjgs.rat.util.HueColorCreator;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class HealthRenderSystem extends IteratingSystem {

    private static float healthBarWidth = 1f;
    private static float healthBarHeight = healthBarWidth * 0.2f;
    private static Vector2 healthBarOrigin = new Vector2(-healthBarWidth/2f, -healthBarHeight/2f);
    private static Vector2 healthBarOffset = new Vector2(0, 0.2f);

    private ComponentMapper<Health> mHealth;
    private ComponentMapper<Transform> mTransform;

    private Sprite outlineSprite;
    private Sprite fillSprite;

    @Wire
    private SpriteBatch batch;

    @Wire
    private OrthographicCamera camera;

    public HealthRenderSystem() {
        super(Aspect.all(Health.class, Transform.class).one(MeshGraphics.class, SpriteGraphics.class));
    }

    @Override
    protected void initialize() {
        Pixmap outlineMap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        outlineMap.setColor(1, 1, 1, 0);
        outlineMap.fill();

        outlineMap.setColor(Color.WHITE);
        outlineMap.drawRectangle(0, 0, 99, 19);
        outlineMap.drawRectangle(1, 1, 97, 17);

        outlineSprite = new Sprite(new Texture(outlineMap));
        outlineSprite.setSize(healthBarWidth, healthBarHeight);

        Pixmap fillMap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        fillMap.setColor(1, 1, 1, 0);
        fillMap.fill();

        fillMap.setColor(Color.WHITE);
        fillMap.fillRectangle(4, 4, 91, 11);

        fillSprite = new Sprite(new Texture(fillMap));
        fillSprite.setSize(healthBarWidth, healthBarHeight);
    }

    @Override
    protected void begin() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void process(int entityId) {
        Health health = mHealth.get(entityId);
        Transform transform = mTransform.get(entityId);

        if(health.drawHealth){
            float percentHealth = health.health / health.maxHealth;

            if(percentHealth > 1) percentHealth = 1;
            if(percentHealth < 0) percentHealth = 0;

            float x = transform.position.x + healthBarOffset.x + healthBarOrigin.x;
            float y = transform.position.y + healthBarOffset.y + healthBarOrigin.y;

            Color color = HueColorCreator.createColor(percentHealth);

            outlineSprite.setColor(color);
            outlineSprite.setPosition(x, y);
            outlineSprite.draw(batch);

            fillSprite.setColor(color);
            fillSprite.setPosition(x, y);
            fillSprite.setSize(healthBarWidth * percentHealth, healthBarHeight);
            fillSprite.draw(batch);

        }
    }

    @Override
    protected void end() {
        batch.end();
    }
}
