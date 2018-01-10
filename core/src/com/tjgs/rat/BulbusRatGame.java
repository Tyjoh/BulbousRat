package com.tjgs.rat;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;
import com.tjgs.rat.builder.entity.EntityGroupBuilder;
import com.tjgs.rat.component.PhysicsController;
import com.tjgs.rat.component.Transform;
import com.tjgs.rat.data.entity.EntityData;
import com.tjgs.rat.data.entity.EntityGroupData;
import com.tjgs.rat.event.EventBlackboard;
import com.tjgs.rat.input.controller.ActionIntentProvider;
import com.tjgs.rat.input.controller.Controller;
import com.tjgs.rat.input.controller.KeyboardController;
import com.tjgs.rat.input.controller.PhysicsIntentProvider;
import com.tjgs.rat.system.*;
import com.tjgs.rat.system.game.*;
import com.tjgs.rat.util.entity.EntityDataFactory;
import com.tjgs.rat.util.physics.PhysicsWorld;

public class BulbusRatGame extends ApplicationAdapter {

    private static final int VIEW_WIDTH = 16;
    private static final int VIEW_HEIGHT = 9;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private World entityWorld;
    private PhysicsWorld physicsWorld;

    private WorldSerializationSystem worldSerializationSystem;
    private EntitySpawnerSystem spawnerSystem;

    private EventBlackboard eventBlackboard;

    private IntMap<Controller> controllerMap;

	@Override
	public void create () {

	    setupGraphics();

		setupControllers();

		//new physics world with no gravity
		physicsWorld = new PhysicsWorld(new Vector2(0, 0), true);

        setupECS();

        //populateWorld();

        worldSerializationSystem.loadWorld("quicksave");

	}

	private void populateWorld(){

        PhysicsController snakeController = new PhysicsController();
        snakeController.controllerId = 0;
        snakeController.linearAccel = 0.4f;
        snakeController.maxLinearSpeed = 3.4f;
        snakeController.angularAccel = 0.5f;
        snakeController.maxAngularSpeed = 2.7f;

        EntityDataFactory entityFactory = new EntityDataFactory();
        EntityGroupData wiggler = entityFactory.getEntityGroupBuilder("Wiggler");

        for(int i = 3; i < 15; i += 3){
            spawnGroup(wiggler, new Vector2(i, 2), i * 0.15f);
        }

        EntityGroupData snaker = entityFactory.getEntityGroupBuilder("Snakey");
        snaker.entities.get(0).components.add(snakeController);

        EntityData triangle = entityFactory.getEntityBuilder("Triangular");

        spawnerSystem.queueEntitySpawn(triangle);

        spawnerSystem.queueEntityGroupSpawn(snaker);
        spawnerSystem.queueEntityGroupSpawn(wiggler);

        for(int i = 1; i < 15; i ++){
            spawnBarrel(new Vector2(i*1.1f, 7 + (i%2) * 0.5f));
        }

        int minX = -4;
        int maxX = 22;

        int minY = -4;
        int maxY = 12;

        for(int i = minX; i <= maxX; i += 2){
            spawnWall(new Vector2(i, maxY));
            spawnWall(new Vector2(i, minY));
        }

        for(int i = minY; i <= maxY; i += 2){
            spawnWall(new Vector2(minX, i));
            spawnWall(new Vector2(maxX, i));
        }
    }

    public void spawnGroup(EntityGroupData groupData, Vector2 pos, float angle){
        EntityGroupBuilder builder = new EntityGroupBuilder(entityWorld);
        Transform transform = new Transform();
        transform.position.set(pos);
        transform.angle = angle;
        builder.build(groupData, transform);
    }

	private void spawnBarrel(Vector2 pos){

	    EntityData barrel = new EntityData();
	    barrel.prefabName = "Barrel";

	    Transform transform = new Transform();
	    transform.position.set(pos);
	    barrel.components.add(transform);

	    spawnerSystem.queueEntitySpawn(barrel);
    }

    private void spawnWall(Vector2 pos){
        EntityData wall = new EntityData();
        wall.prefabName = "WallBlock";

        Transform transform = new Transform();
        transform.position.set(pos);
        wall.components.add(transform);

        spawnerSystem.queueEntitySpawn(wall);
    }

	private void setupGraphics(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEW_WIDTH, VIEW_HEIGHT);
        camera.position.set(VIEW_WIDTH/2f, VIEW_HEIGHT/2f, 0);
    }

    private void setupControllers(){

        KeyboardController keyboardController = new KeyboardController(0);

        controllerMap = new IntMap<Controller>(5);
        controllerMap.put(0, keyboardController);

        //add other controllers to map here

    }

    private void setupECS(){
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder();

        IntMap<PhysicsIntentProvider> pip = getControllers(PhysicsIntentProvider.class);
        IntMap<ActionIntentProvider> aip = getControllers(ActionIntentProvider.class);

        eventBlackboard = new EventBlackboard();

        worldSerializationSystem = new WorldSerializationSystem();
        spawnerSystem = new EntitySpawnerSystem();
        PhysicsControlSystem pcs = new PhysicsControlSystem();
        for(PhysicsIntentProvider provider: pip.values()) {
            provider.getPhysicsIntentRegistry().registerIntentTracker(pcs);
        }

        ActionControlSystem acs = new ActionControlSystem();
        for(ActionIntentProvider provider: aip.values()) {
            provider.getActionIntentRegistry().registerIntentTracker(acs);
        }

        worldConfigBuilder.with(worldSerializationSystem);
        worldConfigBuilder.with(spawnerSystem);
        worldConfigBuilder.with(new InputPollSystem(controllerMap.values().toArray()));
        worldConfigBuilder.with(pcs);
        worldConfigBuilder.with(acs);
        worldConfigBuilder.with(new PhysicsSystem());
        worldConfigBuilder.with(new ProximitySensorSystem());
        worldConfigBuilder.with(new GroupHealSystem());
        worldConfigBuilder.with(new CollisionDamageSystem());
        worldConfigBuilder.with(new HealthSystem());
        worldConfigBuilder.with(new CameraFollowSystem());
        worldConfigBuilder.with(new RenderingSystem());
        worldConfigBuilder.with(new HealthRenderSystem());
        worldConfigBuilder.with(new PhysicsDebugRenderSystem());

        WorldConfiguration worldConfig = worldConfigBuilder.build();

        worldConfig.register(eventBlackboard);
        worldConfig.register(physicsWorld);
        worldConfig.register(camera);
        worldConfig.register(batch);

        entityWorld = new World(worldConfig);
    }

	@Override
	public void render () {

	    entityWorld.setDelta(Gdx.graphics.getDeltaTime());
	    entityWorld.process();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		entityWorld.dispose();
		physicsWorld.world.dispose();
	}

    /**
     * gets a map of all controllers of type T. This is used to pass mappings of
     * various controller implementations to entity systems
     */
    @SuppressWarnings("unchecked")
    private <T> IntMap<T> getControllers(Class<T> tClass){
        IntMap<T> map = new IntMap<T>(5);

        IntMap.Keys keys = controllerMap.keys();

        while (keys.hasNext){
            int key = keys.next();
            Controller controller = controllerMap.get(key);

            if(isType(tClass, controller)){
                Gdx.app.log("BulbusRatGame", "Found a " + tClass.getSimpleName() + " with controller id " + key + "!!!");
                map.put(key, (T) controller);
            }
        }

        return map;
    }

    //Checks if instance can be assigned to tClass type
    private <T> boolean isType(Class<T> tClass, Object instance){
        return instance != null && tClass.isAssignableFrom(instance.getClass());
    }
}
