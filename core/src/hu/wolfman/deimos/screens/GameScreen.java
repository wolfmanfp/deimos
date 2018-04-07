package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.entities.Enemy;
import hu.wolfman.deimos.entities.Player;
import hu.wolfman.deimos.hud.HeadsUpDisplay;
import hu.wolfman.deimos.hud.TouchController;
import hu.wolfman.deimos.input.ControllerInputListener;
import hu.wolfman.deimos.input.KeyboardInputListener;
import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.ContactListener;
import hu.wolfman.deimos.physics.FixtureBuilder;
import hu.wolfman.deimos.utils.MusicManager;
import hu.wolfman.deimos.utils.ResourceManager;

import static com.badlogic.gdx.Application.ApplicationType.Android;
import static hu.wolfman.deimos.Constants.*;
import static hu.wolfman.deimos.physics.B2DConst.*;

/**
 * A játék fő képernyője.
 * Itt zajlanak a játék eseményei, itt jelenik
 * meg a pálya, a játékos és az ellenfelek.
 * @author Farkas Péter
 */
public class GameScreen implements Screen {
    private final Game game;
    private HeadsUpDisplay hud;
    private TouchController touchController;
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private InputMultiplexer multiplexer;

    //Box2D
    private World world;
    private ContactListener contactListener;
    private Box2DDebugRenderer debugRenderer;

    //Tiled pálya
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //Entitások
    private Player player;
    private Array<Enemy> enemies;

    private boolean musicIsMuted = false;
    private boolean debug = false;

    /**
     * A képernyő konstruktora, innen hívódik meg
     * a HUD, a pálya és a játékos inicializálása.
     * @param game A játék fő osztályának objektuma.
     */
    public GameScreen(Game game) {
        this.game = game;
        
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new ContactListener();
        world.setContactListener(contactListener);
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);
        
        if (game.debugMode) {
            debugCamera = new OrthographicCamera();
            debugCamera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);
            debugRenderer = new Box2DDebugRenderer();
        }

        map = new TmxMapLoader().load(MAIN_GAME + TEST_LEVEL);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f / PPM);

        enemies = new Array<>();
        createPlatformsAndEntities();
        
        hud = new HeadsUpDisplay(game, player);
        touchController = new TouchController(game, player);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(touchController.getStage());
        multiplexer.addProcessor(new KeyboardInputListener(player, this));
        Gdx.input.setInputProcessor(multiplexer);
        Controllers.addListener(new ControllerInputListener(player));

        MusicManager.get().play("GameMusic");
    }

    /**
     * A pályán található platformok és entitások létrehozása
     * a pályán definiált objektumok alapján.
     */
    private void createPlatformsAndEntities() {
        for (MapObject object : map.getLayers().get("collision").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new BodyBuilder(world).isStatic()
                .setPosition(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2)
                .addFixture(
                        new FixtureBuilder()
                            .setBoxShape(rect.getWidth(), rect.getHeight())
                            .setFilter(PLATFORM_BIT, (PLAYER_BIT|ENEMY_BIT|BULLET_BIT))
                            .build()
                )
                .build();
        }

        TextureRegion playerTexture = ResourceManager.get().textureRegion("player", "player_standing");
        TextureRegion enemyTexture = new TextureRegion(ResourceManager.get().texture("enemy"));

        for (MapObject object : map.getLayers().get("entities").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String type = object.getProperties().get("type", String.class);

            if (type.equals("player") && player == null) player = new Player(world, playerTexture, rect);
            if (type.equals("enemy")) enemies.add(new Enemy(world, enemyTexture, rect));
        }
    }

    /**
     * A pálya kirajzolása a képernyőre.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     */
    @Override
    public void render(float delta) {
        update(delta);
        
        game.batch.setProjectionMatrix(camera.combined);
        setCameraPosition(camera);
        camera.update();
        
        mapRenderer.setView(camera);
        mapRenderer.render();
        
        game.batch.begin();
        player.draw(game.batch);
        enemies.forEach(enemy -> enemy.draw(game.batch));
        game.batch.end();

        if (debug) {
            game.batch.setProjectionMatrix(debugCamera.combined);
            setCameraPosition(debugCamera);
            debugCamera.update();
            debugRenderer.render(world, debugCamera.combined);
        }

        hud.draw();
        if (Gdx.app.getType() == Android) touchController.draw();
    }

    /**
     * A játék állapotának frissítése.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     */
    private void update(float delta) {
        world.step(delta, 6, 2);
        player.update(delta);
        enemies.forEach(enemy -> enemy.update(delta));
        hud.update();
    }

    public void toggleDebugRenderer() {
        if (game.debugMode) debug = !debug;
    }

    /**
     * A kamera pozíciójának beállítása.
     * @param camera Kameraobjektum
     */
    private void setCameraPosition(OrthographicCamera camera) {
        float playerPositionX = player.getPosX();
        float mapWidth = map.getProperties().get("width", Integer.class) * 
                ((TiledMapTileLayer)map.getLayers().get(0)).getTileWidth() / PPM;
        
        if (playerPositionX < camera.viewportWidth / 2) {
            camera.position.x = camera.viewportWidth / 2;
        }
        else if (playerPositionX > mapWidth - camera.viewportWidth / 2) {
            camera.position.x = mapWidth - camera.viewportWidth / 2;
        }
        else camera.position.x = playerPositionX;
    }
    
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    /**
     * A metódus a memóriát üríti bezáráskor.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }
        hud.dispose();
        touchController.dispose();
    }

}
