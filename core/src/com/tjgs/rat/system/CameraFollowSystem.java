package com.tjgs.rat.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tjgs.rat.component.CameraFollower;
import com.tjgs.rat.component.Transform;

/**
 * Created by Tyler Johnson on 4/27/2017.
 *
 */
public class CameraFollowSystem extends IteratingSystem {

    private ComponentMapper<Transform> mTransform;
    private ComponentMapper<CameraFollower> mCamFollow;

    @Wire
    private OrthographicCamera camera;

    public CameraFollowSystem(){
        super(Aspect.all(CameraFollower.class));
    }

    @Override
    protected void process(int entityId) {

        CameraFollower follower = mCamFollow.get(entityId);
        Vector2 targetPos = mTransform.get(entityId).position;

        Vector2 deltaPos = targetPos.cpy();
        deltaPos.sub(camera.position.x, camera.position.y);

        deltaPos.scl(follower.tweenSpeed);

        if(deltaPos.len() >= follower.tweenCutoff){
            camera.position.add(new Vector3(deltaPos.x, deltaPos.y, 0));
        }

    }
}
