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
import hu.wolfman.deimos.entities.Player.State;
import hu.wolfman.deimos.hud.HeadsUpDisplay;
import hu.wolfman.deimos.hud.TouchController;
import hu.wolfman.deimos.input.ControllerInputListener;
import hu.wolfman.deimos.input.KeyboardInputListener;
import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.ContactListener;
import hu.wolfman.deimos.physics.FixtureBuilder;
import hu.wolfman.deimos.utils.MusicManager;
import hu.wolfman.deimos.utils.ResourceManager;

import static hu.wolfman.deimos.Constants.*;

/**
 * A játék fő képernyője.
 * Itt zajlanak a játék eseményei, itt jelenik
 * meg a pálya, a játékos és az ellenfelek.
 *
 * @author Farkas Péter
 */
public class GameScreen implements Screen {
  private final Game game;
  private HeadsUpDisplay hud;
  private OrthographicCamera camera;
  private OrthographicCamera debugCamera;

  // Input
  private TouchController touchController;
  private InputMultiplexer multiplexer;

  // Box2D
  private World world;
  private ContactListener contactListener;
  private Box2DDebugRenderer debugRenderer;

  // Tiled pálya
  private TiledMap map;
  private OrthogonalTiledMapRenderer mapRenderer;

  // Entitások
  private Player player;
  private Array<Enemy> enemies;

  private boolean debug = false;

  /**
   * Az osztály konstruktora.
   *
   * @param game A játék fő osztályának objektuma.
   */
  public GameScreen(Game game) {
    this.game = game;

    world = new World(new Vector2(0, -9.81f), true);
    contactListener = new ContactListener();
    world.setContactListener(contactListener);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);

    if (game.isDebugModeOn()) {
      debugCamera = new OrthographicCamera();
      debugCamera.setToOrtho(false, WIDTH / PPM, HEIGHT / PPM);
      debugRenderer = new Box2DDebugRenderer();
    }

    map = new TmxMapLoader().load(MAIN_GAME + TEST_LEVEL);
    mapRenderer = new OrthogonalTiledMapRenderer(map, 1f / PPM);

    enemies = new Array<>();
    createPlatformsAndEntities();

    hud = new HeadsUpDisplay(player, game.batch, game.isDebugModeOn());
    touchController = new TouchController(player, game.batch);

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
    for (MapObject object :
        map.getLayers().get("collision").getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      new BodyBuilder(world).isStatic()
          .setPosition(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2)
          .addFixture(
              new FixtureBuilder()
                  .setBoxShape(rect.getWidth(), rect.getHeight())
                  .setFilter(PLATFORM_BIT, PLAYER_BIT | ENEMY_BIT | BULLET_BIT | ENEMY_BULLET_BIT)
                  .build()
          )
          .build();
    }

    TextureRegion playerTexture = ResourceManager.get().textureRegion("player", "player_standing");
    TextureRegion enemyTexture = ResourceManager.get().textureRegion("enemy", "enemy_idle");

    for (MapObject object :
        map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();
      String type = object.getProperties().get("type", String.class);

      if (type.equals("player") && player == null) {
        player = new Player(world, playerTexture, rect);
      }
      if (type.equals("enemy")) {
        enemies.add(new Enemy(world, enemyTexture, rect));
      }
      if (type.equals("levelEnd")) {
        new BodyBuilder(world).isStatic()
            .setPosition(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2)
            .addFixture(
                new FixtureBuilder()
                    .setBoxShape(rect.getWidth(), rect.getHeight())
                    .setFilter(LEVEL_END_BIT, PLAYER_BIT | ENEMY_BIT | BULLET_BIT | ENEMY_BULLET_BIT)
                    .build()
            )
            .build();
      }
    }
  }

  /**
   * A pálya kirajzolása a képernyőre.
   *
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
    enemies.forEach(enemy -> enemy.draw(game.batch));
    player.draw(game.batch);
    game.batch.end();

    if (debug) {
      game.batch.setProjectionMatrix(debugCamera.combined);
      setCameraPosition(debugCamera);
      debugCamera.update();
      debugRenderer.render(world, debugCamera.combined);
    }

    hud.draw();
    if (game.isRunningOnPhone()) {
      touchController.draw();
    }

    checkState();
  }

  /**
   * A játék állapotának frissítése.
   *
   * @param delta Két frissítés között eltelt idő (általában 1/60 s).
   */
  private void update(float delta) {
    world.step(delta, 6, 2);
    player.update(delta);
    enemies.forEach(enemy -> enemy.update(delta));
    hud.update();
  }

  /**
   * Ellenőrzi, hogy a játékos nyert-e vagy vesztett,
   * és megadja, hogy mi történjen ezekben az esetekben.
   */
  private void checkState() {
    if (player.currentState == State.DEAD && player.getStateTimer() > 1) {
      MusicManager.get().stop();
      Controllers.clearListeners();
      dispose();
      game.setScreen(
          new EndScreen(game, ResourceManager.get().localeString("game", "youDied"))
      );
    }

    boolean enemiesAreDead = true;
    for (Enemy enemy: enemies) {
      if (!enemy.isDead()) {
        enemiesAreDead = false;
        break;
      }
    }

    if (player.hasReachedEndOfLevel() && enemiesAreDead) {
      MusicManager.get().stop();
      Controllers.clearListeners();
      dispose();
      game.setScreen(
          new EndScreen(game, ResourceManager.get().localeString("game", "youWin"))
      );
    } else {
      player.setHasReachedEndOfLevel(false);
    }
  }

  /**
   * A Box2D debug renderert kapcsolja ki/be.
   */
  public void toggleDebugRenderer() {
    if (game.isDebugModeOn()) {
      debug = !debug;
    }
  }

  /**
   * A kamera pozíciójának beállítása.
   *
   * @param camera Kameraobjektum
   */
  private void setCameraPosition(OrthographicCamera camera) {
    float playerPositionX = player.getPosX();
    float mapWidth = map.getProperties().get("width", Integer.class)
        * ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth() / PPM;

    if (playerPositionX < camera.viewportWidth / 2) {
      camera.position.x = camera.viewportWidth / 2;
    } else if (playerPositionX > mapWidth - camera.viewportWidth / 2) {
      camera.position.x = mapWidth - camera.viewportWidth / 2;
    } else {
      camera.position.x = playerPositionX;
    }
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

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void show() {}

  @Override
  public void hide() {}

}
