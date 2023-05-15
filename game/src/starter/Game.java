package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.SystemController;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.xp.XPComponent;
import ecs.entities.*;

import ecs.systems.*;
import graphic.DungeonCamera;
import graphic.Painter;
import graphic.hud.PauseMenu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import level.IOnLevelLoader;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.tools.LevelSize;
import tools.Constants;
import tools.Point;

/** The heart of the framework. From here all strings are pulled. */
public class Game extends ScreenAdapter implements IOnLevelLoader {

    private final LevelSize LEVELSIZE = LevelSize.MEDIUM;

    /**
     * The batch is necessary to draw ALL the stuff. Every object that uses draw need to know the
     * batch.
     */
    protected SpriteBatch batch;

    /** Contains all Controller of the Dungeon */
    protected List<AbstractController<?>> controller;

    public static DungeonCamera camera;
    /** Draws objects */
    protected Painter painter;

    protected LevelAPI levelAPI;
    /** Generates the level */
    protected IGenerator generator;

    private boolean doSetup = true;
    private static boolean paused = false;

    /** All entities that are currently active in the dungeon */
    private static final Set<Entity> entities = new HashSet<>();
    /** All entities to be removed from the dungeon in the next frame */
    private static final Set<Entity> entitiesToRemove = new HashSet<>();
    /** All entities to be added from the dungeon in the next frame */
    private static final Set<Entity> entitiesToAdd = new HashSet<>();

    /** List of all Systems in the ECS */
    public static SystemController systems;

    // is a TileLevel Objekt
    public static ILevel currentLevel;
    private static PauseMenu<Actor> pauseMenu;
    private static Entity hero;
    private static Monster monster;
    private Logger gameLogger;

    private PatrouilleWalk p;

    private static List<Entity> listeWolf = new ArrayList<>();
    private static List<Entity> listeZombie = new ArrayList<>();
    private static List<Entity> listeMumie = new ArrayList<>();

    public static Tombstone t;
    public static NpcGhost npc;
    private Save save;
    private static int selectOneMonster;
    private static int despawnOrSpawn;

    public static void main(String[] args) {
        // start the game
        try {
            Configuration.loadAndGetConfiguration("dungeon_config.json", KeyboardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DesktopLauncher.run(new Game());
    }

    /**
     * Main game loop. Redraws the dungeon and calls the own implementation (beginFrame, endFrame
     * and onLevelLoad).
     *
     * @param delta Time since last loop.
     */
    @Override
    public void render(float delta) {
        if (doSetup) setup();
        batch.setProjectionMatrix(camera.combined);
        frame();
        clearScreen();
        levelAPI.update();
        controller.forEach(AbstractController::update);
        camera.update();
//        System.out.println(currentLevel.toString());
    }

    /** Called once at the beginning of the game. */
    protected void setup() {
        doSetup = false;
        controller = new ArrayList<>();
        setupCameras();
        painter = new Painter(batch, camera);
        generator = new RandomWalkGenerator();
        levelAPI = new LevelAPI(batch, painter, generator, this);
        initBaseLogger();
        gameLogger = Logger.getLogger(this.getClass().getName());
        systems = new SystemController();
        controller.add(systems);
        pauseMenu = new PauseMenu<>();
        controller.add(pauseMenu);
        hero = new Hero();
        levelAPI = new LevelAPI(batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
        save = new Save();
        try {
            save.laden(levelAPI);
            this.onLevelLoad();
        } catch (FileNotFoundException e) {
            levelAPI.loadLevel(LEVELSIZE);
        }
        createSystems();
    }

    /** Called at the beginning of each frame. Before the controllers call <code>update</code>. */
    protected void frame() {
        setCameraFocus();
        manageEntitiesSets();
        getHero().ifPresent(this::loadNextLevelIfEntityIsOnEndTile);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) togglePause();
        if(t != null){
            if(despawnOrSpawn == 1) {
                t.despawnOneKindOfMonster();
            }
            else{
                t.spawnNewMonster();
            }
        }
    }

    @Override
    public void onLevelLoad() {
        currentLevel = levelAPI.getCurrentLevel();
        entities.clear();
        getHero().ifPresent(this::placeOnLevelStart);
        LevelAPI.addlevelnummer();

        selectOneMonster = (int) (Math.random()*3+1);
        despawnOrSpawn = (int) (Math.random()*2+1);
        spawnMonster();
        spawnTombstone();
        getHero().ifPresent(this::increaseHeroXP);
        save.speichern(currentLevel);
    }

    public void spawnTombstone(){
        int x = (int) (Math.random()*3+1);
        if(x == 1){
            npc = new NpcGhost();
            t = new Tombstone(npc);
        }
        else{
            t = null;
        }
    }

    private void increaseHeroXP(Entity hero) {
        XPComponent xcp =
            (XPComponent)
                ((Hero)hero).getComponent(XPComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("XPComponent"));
        xcp.addXP(40);
    }

    /** Spawns a random number of monsters and types*/
    public void spawnMonster(){
        for(int i = 0; i < Math.random()*3+LevelAPI.getlevelnummer()/2; i++){
            int x = (int) (Math.random()*3+1);
            switch (x) {
                case (1) :
                    Wolf w = new Wolf();
                    listeWolf.add(w);
                    break;
                case (2) :
                    Zombie z = new Zombie();
                    listeZombie.add(z);
                    break;
                case (3) :
                    Mumie m = new Mumie();
                    listeMumie.add(m);
                    break;
            }
        }
    }

    /**This methode is used to despawn a random kind of monsters*/
    public static void despawnMonster(){
    switch (selectOneMonster) {
            case (1) :
                for (Entity entity : listeWolf) {
                    Game.removeEntity(entity);
                }
                listeWolf.clear();
                break;
            case (2) :
                for (Entity entity : listeZombie) {
                    Game.removeEntity(entity);
                }
                listeZombie.clear();
                break;
            case (3) :
                for (Entity entity : listeMumie) {
                    Game.removeEntity(entity);
                }
                listeMumie.clear();
                break;
        }
    }

    public static void spawnOneZombie(){
        if(despawnOrSpawn == 2) {
            Zombie z = new Zombie();
            listeZombie.add(z);
            despawnOrSpawn = 3;
        }
    }

    private void manageEntitiesSets() {
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        for (Entity entity : entitiesToRemove) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was deleted.");
        }
        for (Entity entity : entitiesToAdd) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was added.");
        }
        entitiesToRemove.clear();
        entitiesToAdd.clear();
    }

    private void setCameraFocus() {
        if (getHero().isPresent()) {
            PositionComponent pc =
                    (PositionComponent)
                            getHero()
                                    .get()
                                    .getComponent(PositionComponent.class)
                                    .orElseThrow(
                                            () ->
                                                    new MissingComponentException(
                                                            "PositionComponent"));
            camera.setFocusPoint(pc.getPosition());

        } else camera.setFocusPoint(new Point(0, 0));
    }

    private void loadNextLevelIfEntityIsOnEndTile(Entity hero) {
        if (isOnEndTile(hero)) levelAPI.loadLevel(LEVELSIZE);
    }

    private boolean isOnEndTile(Entity entity) {
        PositionComponent pc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        Tile currentTile = currentLevel.getTileAt(pc.getPosition().toCoordinate());
        return currentTile.equals(currentLevel.getEndTile());
    }

    private void placeOnLevelStart(Entity hero) {
        entities.add(hero);
        PositionComponent pc =
                (PositionComponent)
                        hero.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        pc.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());
    }

    /** Toggle between pause and run */
    public static void togglePause() {
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (pauseMenu != null) {
            if (paused) pauseMenu.showMenu();
            else pauseMenu.hideMenu();
        }
    }

    /**
     * Given entity will be added to the game in the next frame
     *
     * @param entity will be added to the game next frame
     */
    public static void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    /**
     * Given entity will be removed from the game in the next frame
     *
     * @param entity will be removed from the game next frame
     */
    public static void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    /**
     * @return Set with all entities currently in game
     */
    public static Set<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Set with all entities that will be added to the game next frame
     */
    public static Set<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
    }

    /**
     * @return Set with all entities that will be removed from the game next frame
     */
    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * @return the player character, can be null if not initialized
     */
    public static Optional<Entity> getHero() {
        return Optional.ofNullable(hero);
    }

//    public static void getHero(Entity hero) {
//        Game.hero = hero;
//    }

    /**
     * set the reference of the playable character careful: old hero will not be removed from the
     * game
     *
     * @param hero new reference of hero
     */
    public static void setHero(Entity hero) {
        Game.hero = hero;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void setupCameras() {
        camera = new DungeonCamera(null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.zoom = Constants.DEFAULT_ZOOM_FACTOR;

        // See also:
        // https://stackoverflow.com/questions/52011592/libgdx-set-ortho-camera
    }

    // creates Systems, but also decides in which order they are updated
    private void createSystems() {
        new VelocitySystem();
        new DrawSystem(painter);
        new PlayerSystem();
        new AISystem();
        new CollisionSystem();
        new HealthSystem();
        new XPSystem();
        new SkillSystem();
        new ProjectileSystem();
        new ManaSystem();
        new XPSystem();
    }
}
