package hu.wolfman.deimos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hu.wolfman.deimos.screens.PlayScreen;
import hu.wolfman.deimos.tools.Logger;

/**
 * A játék fő osztálya, itt fut a game loop.
 * @author Farkas Péter
 */
public class Game extends com.badlogic.gdx.Game {
    public SpriteBatch batch;
    public AssetManager manager;
    
    public boolean debugMode = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        loadAssets();
        setScreen(new PlayScreen(this));
        Logger.get().log("A játék sikeresen elindult.");
    }
    
    private void loadAssets() {
        Resources.get().setGame(Constants.MAINGAME);
        Resources.get().finishLoading();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        manager.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
    }
    
    @Override
    public void resume() {
        super.resume(); 
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height); 
    }
}
