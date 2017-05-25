package hu.wolfman.deimos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A játék fő osztálya, itt fut a game loop.
 * @author Farkas Péter
 */
public class Game extends ApplicationAdapter {
    SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}
