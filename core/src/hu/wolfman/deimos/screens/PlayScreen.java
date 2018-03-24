package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import hu.wolfman.deimos.utils.ResourceManager;
import hu.wolfman.deimos.entities.Enemy;
import hu.wolfman.deimos.entities.Player;
import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.ContactListener;
import hu.wolfman.deimos.physics.FixtureBuilder;
import hu.wolfman.deimos.utils.Logger;

import static hu.wolfman.deimos.Constants.*;
import static hu.wolfman.deimos.physics.BoxConst.*;

/**
 * A játék fő képernyője.
 * Itt zajlanak a játék eseményei, itt jelenik
 * meg a pálya, a játékos és az ellenfelek.
 * @author Farkas Péter
 */
public class PlayScreen implements Screen {
    private final Game game;
    private HeadsUpDisplay hud;
    private OnScreenController controller;
    private Music music;
    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;

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
    public PlayScreen(Game game) {
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
        controller = new OnScreenController(game);
        
        music = ResourceManager.get().music("GameMusic");
        music.setLooping(true);
        music.play();
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
                            .setPolygonShape(rect.getWidth() / 2, rect.getHeight() / 2, 0, 0)
                            .setFilter(GROUND_BIT, (PLAYER_BIT|ENEMY_BIT|BULLET_BIT))
                            .build()
                )
                .build();
        }
        
        for (MapObject object : map.getLayers().get("entities").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            String type = object.getProperties().get("type", String.class);

            if (type.equals("player") && player == null) player = new Player(world, rect);
            if (type.equals("enemy")) enemies.add(new Enemy(world, rect));
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
        for (Enemy enemy : enemies) {
            enemy.draw(game.batch);
        }
        game.batch.end();

        if (debug) {
            game.batch.setProjectionMatrix(debugCamera.combined);
            setCameraPosition(debugCamera);
            debugCamera.update();
            debugRenderer.render(world, debugCamera.combined);
        }

        hud.draw();

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    /**
     * A játék állapotának frissítése.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     */
    private void update(float delta) {
        handleInput();
        world.step(delta, 6, 2);
        player.update(delta);
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        hud.update();
    }

    /**
     * Meghatározza, hogy megadott billentyű lenyomása
     * esetén mi történjen.
     */
    private void handleInput() {
        if (player.currentState != Player.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Keys.UP) || controller.isJumpPressed()) {
                ResourceManager.get().sound("jump").play();
                player.jump();
            }
            if ((Gdx.input.isKeyPressed(Keys.RIGHT) || controller.isRightPressed())
                    && player.getVelocityX() <= 2) {
                player.moveRight();
            }
            if ((Gdx.input.isKeyPressed(Keys.LEFT) || controller.isLeftPressed())
                    && player.getVelocityY() >= -2) {
                player.moveLeft();
            }
            if (Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT) || controller.isShootPressed()) {
                //player.fire();
            }
            if (Gdx.input.isKeyJustPressed(Keys.M)) {
                if (!musicIsMuted) {
                    music.setVolume(0.0f);
                    musicIsMuted = true;
                }
                else {
                    music.setVolume(1.0f);
                    musicIsMuted = false;
                }
            }
            if (Gdx.input.isKeyJustPressed(Keys.PLUS)) {
                float currentVolume = music.getVolume();
                if (currentVolume < 1.0f) {
                    music.setVolume(currentVolume + 0.1f);
                }
            }
            if (Gdx.input.isKeyJustPressed(Keys.MINUS)) {
                float currentVolume = music.getVolume();
                if (currentVolume > 0.0f) {
                    music.setVolume(currentVolume - 0.1f);
                }
                else music.setVolume(0.0f);
            }
            if (game.debugMode && Gdx.input.isKeyJustPressed(Keys.D)) {
                debug = !debug;
            }
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Logger.log("Kilépés a játékból");
            Gdx.app.exit();
        }
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
        controller.dispose();
    }

}
