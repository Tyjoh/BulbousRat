package com.tjgs.rat.system;


import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.tjgs.rat.component.SpriteGraphics;
import com.tjgs.rat.component.MeshGraphics;
import com.tjgs.rat.component.Transform;

/**
 * Created by Tyler Johnson on 4/2/2017.
 *
 */

public class RenderingSystem extends IteratingSystem {

    private ComponentMapper<SpriteGraphics> mGraphics;
    private ComponentMapper<Transform> mPosition;
    private ComponentMapper<MeshGraphics> mMeshGraphics;

    @Wire
    private SpriteBatch batch;

    @Wire
    private OrthographicCamera camera;

    private ShaderProgram colorShader;
    private ShaderProgram textureShader;

    private Queue<Integer> graphicsQueue;
    private Queue<Integer> meshGraphicsQueue;

    public RenderingSystem() {
        super(Aspect.all(Transform.class).one(SpriteGraphics.class, MeshGraphics.class));

        colorShader = new ShaderProgram(Gdx.files.internal("shaders/color.vertex.glsl"), Gdx.files.internal("shaders/color.fragment.glsl"));
        textureShader = new ShaderProgram(Gdx.files.internal("shaders/texture.vertex.glsl"), Gdx.files.internal("shaders/texture.fragment.glsl"));
        System.out.println("texture shader compiled: " + (Gdx.gl20.glGetError() == GL20.GL_NO_ERROR));

        graphicsQueue = new Queue<Integer>();
        meshGraphicsQueue = new Queue<Integer>();

    }

    @Override
    protected void begin() {

        graphicsQueue.clear();
        meshGraphicsQueue.clear();

    }

    @Override
    protected void process(int entityId) {
        if(mGraphics.has(entityId)) {
            graphicsQueue.addLast(entityId);
        }else if(mMeshGraphics.has(entityId)){
            meshGraphicsQueue.addLast(entityId);
        }
    }

    protected void drawGraphics(int entityId){
        SpriteGraphics spriteGraphics = mGraphics.get(entityId);
        Transform transform = mPosition.get(entityId);
        Vector2 vPos = transform.position;

        spriteGraphics.getSprite().setCenter(vPos.x, vPos.y);
        spriteGraphics.getSprite().setRotation((float) Math.toDegrees(transform.angle));//draw rotation of sprite as well
        spriteGraphics.getSprite().draw(batch);
    }

    protected void drawMeshGraphics(int entityId){
        MeshGraphics graphics = mMeshGraphics.get(entityId);
        Transform position = mPosition.get(entityId);

        //setup mesh transformation
        Vector2 vPos = position.position;
        Matrix4 transform = new Matrix4();
        transform.translate(vPos.x, vPos.y, 0);
        transform.rotateRad(0, 0, 1, position.angle);
        transform.scl(1);

        ShaderProgram shader;

        if(graphics.useTexture()){
            shader = textureShader;
        }else{
            shader = colorShader;
        }

        shader.begin();

        if(graphics.useTexture()){
            graphics.getTexture().bind();
            shader.setUniformi("u_texture", 0);
        }

        shader.setUniformMatrix("u_projTrans", camera.combined);
        shader.setUniformMatrix("u_transform", transform);

        graphics.getMesh().render(shader, GL20.GL_TRIANGLES, 0, graphics.getNumInidces(), true);

        shader.end();

    }

    @Override
    protected void end() {

        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);

        for(int i: graphicsQueue){
            drawGraphics(i);
        }

        batch.end();

        for(int i:meshGraphicsQueue){
            drawMeshGraphics(i);
        }
    }
}
