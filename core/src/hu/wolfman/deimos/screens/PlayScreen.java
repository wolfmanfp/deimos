package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.entities.Enemy;
import hu.wolfman.deimos.entities.Player;

/**
 *
 * @author Farkas Péter
 */
public class PlayScreen implements Screen {
    private Game game;
    private Music music;
    
    private OrthographicCamera gamecam;
    private Box2DDebugRenderer debugRenderer;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    
    private Player player;
    private Array<Enemy> enemies;
    
    private boolean musicIsMuted = false;
    private boolean debug = false;

    public PlayScreen(Game game) {
        this.game = game;
    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
    }
    
    private void update(float delta) {
        handleInput();
    }
    
    private void handleInput() {
        if (player.currentState != Player.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                player.jump();
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
                player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
                player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)) {
                player.fire();
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
            if (game.debugMode && Gdx.input.isKeyJustPressed(Keys.D)) {
                debug = !debug;
            }
        }
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        
        
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
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    

}
