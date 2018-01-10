package com.tjgs.rat.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.tjgs.rat.util.physics.PhysicsWorld;

/**
 * Created by Tyler Johnson on 4/12/2017.
 *
 */
public class PhysicsDebugRenderSystem extends BaseSystem{

    @Wire
    private PhysicsWorld physicsWorld;

    private Box2DDebugRenderer debugRenderer;

    @Wire
    private OrthographicCamera camera;

    private boolean draw;

    public PhysicsDebugRenderSystem(){
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawVelocities(true);
    }

    @Override
    protected void processSystem() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            draw = !draw;
        }

        if(draw){
            debugRenderer.render(physicsWorld.world, camera.combined);
        }

    }
}
